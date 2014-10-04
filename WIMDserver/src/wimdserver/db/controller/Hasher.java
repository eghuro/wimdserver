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

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Hasher {
    public static String saltPwd(String pwd){
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
    
    public static String hashPwd(String saltPwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        byte[] hash = getHash(1024,saltPwd);
        return new String(hash,Charset.forName("UTF-8"));
    }
    
    public static byte[] getHash(int iterationNb, String saltedPwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        digest.reset();
        byte[] hash = digest.digest(saltedPwd.getBytes("UTF-8"));
        for(int i=0;i<iterationNb;i++){
            digest.reset();
            hash = digest.digest(hash);
        }
        return hash;
    }
    
    private static String convert(byte[] b) throws IllegalArgumentException{
        StringBuilder sb = new StringBuilder(b.length);
        for(int i=0;i<b.length;i++){
            if(b[i]<0) throw new IllegalArgumentException();
            sb.append((char)b[i]);
        }
        return sb.toString();
    }
}
