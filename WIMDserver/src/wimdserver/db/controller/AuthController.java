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
    public AuthController(){
        authDB=new AuthDB();
    }
    public void SetUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String salt = Hasher.saltPwd(pwd);
        String saltPwd = salt+pwd;
        String hash = Hasher.hashPwd(saltPwd);
        authDB.SetUser(name, hash, salt);
    }
    
    public boolean AuthenticateUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(authDB.HasName(name)){
            String hashStored = authDB.GetHashForName(name);
            String salt = authDB.GetSaltForName(name);
            String salted = salt+pwd;
            byte[] hash = Hasher.getHash(1024,salted);
            return new String(hash,Charset.forName("UTF-8")).equals(hashStored);
        }else return false;
    }
    
    public String GetSessionID(String name){
        String SID=SessionIDFactory.INSTANCE.GetSessionID();//musi byt unikatni
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 3);//Session na 3 hodiny
        authDB.RegisterSID(SID, name, c.getTime());
        return SID;
    }
    
    public boolean IsAuthenticated(String SID){
        Date validity = authDB.GetSessionValidity(SID);
        if(validity==null) return false;
        if(validity.after(new Date())) return true;
        else{
            authDB.DeregisterSID(SID);
            return false;
        }
    }
    
    public String GetUID(String SID){
        return authDB.GetUID(SID);
    }
}
