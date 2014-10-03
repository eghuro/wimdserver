/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dostane UID a PWD a rekne JO/NE
 * TODO: prepsat na pouze hashmap + SetUser, Authenticate -> co dostane, to napise; zbytek to AuthControlleru
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthDB {
    
    private final ConcurrentHashMap<String,SaltRec> pwds;
    private final ConcurrentHashMap<String,SessionRec> sessions;
    
    
    public AuthDB() {
        this.pwds = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
    }
    
    public void SetUser(String name, String phash, String salt){
        pwds.put(name,new SaltRec(phash,salt));
    }
    
    public Boolean HasName(String name){
        return pwds.containsKey(name);
    }
    
    public String GetHashForName(String name){
        SaltRec sr = pwds.get(name);
        if(sr!=null)
            return sr.hash;
        else return null;
    }
    
    public String GetSaltForName(String name){
        SaltRec sr = pwds.get(name);
        if(sr!=null)
            return sr.salt;
        else return null;
    }
    
    public void RegisterSID(String sid,String uid,Date validity){
        sessions.put(sid,new SessionRec(uid,validity));
    }
    
    public Date GetSessionValidity(String sid){
        SessionRec sr=sessions.get(sid);
        if(sr!=null)
            return sr.validity;
        else return null;
    }
    
    public void DeregisterSID(String sid){
        sessions.remove(sid);
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
