/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import wimdserver.db.sync.model.AuthSalt;
import wimdserver.db.sync.model.AuthSession;

/**
 * Dostane UID a PWD a rekne JO/NE
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthDB {
    
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
    
    public synchronized AuthSalt[] GetSaltTable(){
        AuthSalt[] table = new AuthSalt[pwds.size()];
        
        int i=0;
        for(Entry<String,SaltRec> e : pwds.entrySet()){
            table[i++] = new AuthSalt(e.getKey(),e.getValue().hash,e.getValue().salt);
        }
        
        return table;
    }
    
    public synchronized AuthSession[] GetSessionTable(){
        AuthSession[] table = new AuthSession[sessions.size()];
        
        int i=0;
        for(Entry<String,SessionRec> e:sessions.entrySet()){
            table[i++] = new AuthSession(e.getKey(),e.getValue().UID,e.getValue().validity);
        }
        
        return table;
    }
}
