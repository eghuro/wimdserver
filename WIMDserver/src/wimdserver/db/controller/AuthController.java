/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import wimdserver.db.model.AuthDB;
import wimdserver.db.model.SessionIDFactory;

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
        String salt = saltPwd(pwd);
        String saltPwd = salt+pwd;
        String hash = hashPwd(saltPwd);
        authDB.SetUser(name, hash, salt);
    }
    
    public boolean AuthenticateUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(authDB.HasName(name)){
            String hashStored = authDB.GetHashForName(name);
            String salt = authDB.GetSaltForName(name);
            String salted = salt+pwd;
            byte[] hash = getHash(1024,salted);
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
    
    private String saltPwd(String pwd){
        //vygeneruji sul
        //sul ma alespon 32 znaku
        //sul dorovnava heslo na 128 znaku
        int length=(pwd.length()>128)?32:(128-pwd.length());
        byte[] chain = new byte[length];
        SecureRandom sr;
        sr = new SecureRandom();
        sr.nextBytes(chain);
        return convert(chain);//ASCII
    }
    
    private String hashPwd(String saltPwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        byte[] hash = getHash(1024,saltPwd);
        return new String(hash,Charset.forName("UTF-8"));
    }
    
    private byte[] getHash(int iterationNb, String saltedPwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        byte[] hash = digest.digest(saltedPwd.getBytes("UTF-8"));
        for(int i=0;i<iterationNb;i++){
            digest.reset();
            hash = digest.digest(hash);
        }
        return hash;
    }
    
    private String convert(byte[] b) throws IllegalArgumentException{
        StringBuilder sb = new StringBuilder(b.length);
        for(int i=0;i<b.length;i++){
            if(b[i]<0) throw new IllegalArgumentException();
            sb.append((char)b[i]);
        }
        return sb.toString();
    }
    
     private class SaltRec{
        public String hash,salt;
        public SaltRec(String hash,String salt){
            this.hash=hash;
            this.salt=salt;
        }
    }
}
