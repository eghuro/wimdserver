/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import wimdserver.db.sync.DBRecord;
import wimdserver.db.sync.ISynchronizable;

/**
 * Dostane UID a PWD a rekne JO/NE
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthDB implements ISynchronizable {
    
    private final ConcurrentHashMap<String,SaltRec> pwds;
    private final ConcurrentHashMap<String,SessionRec> sessions;
    
    
    public AuthDB() {
        this.pwds = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
    }
    
    public synchronized void setUser(String name, String phash, String salt){
        pwds.put(name,new SaltRec(phash,salt));
    }
    
    public synchronized Boolean hasName(String name){
        return pwds.containsKey(name);
    }
    
    public synchronized String getHashForName(String name){
        SaltRec sr = pwds.get(name);
        if(sr!=null)
            return sr.hash;
        else return null;
    }
    
    public synchronized String getSaltForName(String name){
        SaltRec sr = pwds.get(name);
        if(sr!=null)
            return sr.salt;
        else return null;
    }
    
    public synchronized void registerSID(String sid,String uid,Date validity){
        sessions.put(sid,new SessionRec(uid,validity));
    }
    
    public synchronized Date getSessionValidity(String sid){
        SessionRec sr=sessions.get(sid);
        if(sr!=null)
            return sr.validity;
        else return null;
    }
    
    public synchronized void deregisterSID(String sid){
        sessions.remove(sid);
    }
    
    public synchronized String getUID(String SID){
        if(sessions.containsKey(SID)){
            SessionRec sr=sessions.get(SID);
            if(sr!=null)
                return sr.UID;
            else return null;
        }else return null;
    }

    @Override
    public synchronized DBRecord[] export() {
        DBRecord[] db = new DBRecord[pwds.size()*2];
        int i=0;
        for(Entry<String,SaltRec> e:pwds.entrySet()){
            db[i]=new DBRecord("authHashes",e.getKey(),e.getValue().hash);
            db[i+1] = new DBRecord("authSalts",e.getKey(),e.getValue().salt);
            i=i+2;
        }
        return db;
    }

    @Override
    public synchronized void load(DBRecord[] data) {
        for(DBRecord d:data){
            SaltRec sr;
            if(pwds.containsKey(d.getName())){
                sr=pwds.get(d.getName());
                pwds.remove(d.getName());
            }else{
                sr = new SaltRec(null,null);
            }
            switch (d.getTable()) {
                case "authHashes":
                    sr.hash=d.getValue();
                    break;
                case "authSalts":
                    sr.salt=d.getValue();
                    break;
            }
            pwds.put(d.getName(), sr);
        }
    }
    
    private class SaltRec{
        public String hash,salt;
        public SaltRec(String hash,String salt){
            this.hash=hash;
            this.salt=salt;
        }
    }
    
    private class SessionRec{
        public String UID;
        public Date validity;
        public SessionRec(String UID,Date validity){
            this.UID=UID;
            this.validity=validity;
        }
    }
}
