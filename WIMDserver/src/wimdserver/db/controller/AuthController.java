/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import wimdserver.db.model.AuthDB;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthController {
    
    private final AuthDB authDB;
    public AuthController(AuthDB authDB){
        this.authDB=authDB;
    }
    public synchronized void setUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String salt = Hasher.saltPwd(pwd);
        String saltPwd = salt+pwd;
        String hash = Hasher.hashPwd(saltPwd);
        authDB.setUser(name, hash, salt);
    }
    
    public synchronized boolean authenticateUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(authDB.hasName(name)){
            String hashStored = authDB.getHashForName(name);
            String salt = authDB.getSaltForName(name);
            String salted = salt+pwd;
            byte[] hash = Hasher.getHash(1024,salted);
            return new String(hash,Charset.forName("UTF-8")).equals(hashStored);
        }else return false;
    }
    
    public synchronized String getSessionID(String name){
        String SID=SessionIDFactory.INSTANCE.getSessionID();//musi byt unikatni
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 3);//Session na 3 hodiny
        authDB.registerSID(SID, name, c.getTime());
        return SID;
    }
    
    public synchronized boolean isAuthenticated(String SID){
        Date validity = authDB.getSessionValidity(SID);
        if(validity==null) return false;
        if(validity.after(new Date())) return true;
        else{
            authDB.deregisterSID(SID);
            return false;
        }
    }
    
    public synchronized String getUID(String SID){
        return authDB.getUID(SID);
    }
}
