/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthDB {

    private final ConcurrentHashMap<String,SaltRec> pwds;
    
    
    public AuthDB() {
        this.pwds = new ConcurrentHashMap<>();
    }
    
    public void SetUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        SaltRec sr = saltNHashPwd(pwd);
        pwds.put(name, sr);
    }
    
    public boolean AuthenticateUser(String name,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        SaltRec sr = pwds.get(name);
        if(sr==null) return false;//spatne uid
        String salted = sr.salt+pwd;
        byte[] hash = getHash(1024,salted);
        return new String(hash,Charset.forName("UTF-8")).equals(sr.hash);
    }
    
    private SaltRec saltNHashPwd(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //vygeneruji sul
        //sul ma alespon 32 znaku
        //sul dorovnava heslo na 128 znaku
        
        int length=(pwd.length()>128)?32:(128-pwd.length());
        byte[] chain = new byte[length];
        SecureRandom sr = SecureRandom.getInstance("");//TODO
        sr.nextBytes(chain);
        String salt=convert(chain);//ASCII
        String salted = salt+pwd;
        byte[] hash = getHash(1024,salted);
        String store = new String(hash,Charset.forName("UTF-8"));
        SaltRec sar = new SaltRec(store,salt);
        return sar;
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
