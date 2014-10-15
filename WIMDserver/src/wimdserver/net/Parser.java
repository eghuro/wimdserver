/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.net;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wimdserver.db.controller.DatabaseController;
import wimdserver.db.controller.NewDeviceReturnRecord;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Parser {
    String result;
    
    final String AUTH="AUTH";
    final String REC="REC";
    final String DEV="DEV";
    
    final String ERR="ERR";//exception
    final String FAIL="FAIL";//login
    final String OK="OK";
    
    final DatabaseController DC;
    
    public Parser(DatabaseController dc){
        this.DC=dc;
    }
    public synchronized ParseResult parse(String s){
         if (s.startsWith(REC)){
             //CHYBA - dostal 1 slovo!!
             Pattern p1 = Pattern.compile("REC SID ([:alnum:]+) OTP ([:alnum:]+) DID ([:alnum:]+) COORD (.+)");//TODO
             Matcher m1 = p1.matcher(s);
             String sid = m1.group(1);
             String otp = m1.group(2);
             String did_s = m1.group(3);
             int did = Integer.parseInt(did_s);
             String loc = m1.group(4);
             try {
                 DC.newRecord(sid, did, otp, loc);
             } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                 Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                 this.result=ERR;
                 return ParseResult.RESULT;
             }
        } else if (s.startsWith(DEV)){
            //CHYBA - dostal 1 slovo
            Pattern p2 = Pattern.compile("DEV ([:alnum:]+) ([:alnum:]+)");
            Matcher m2 = p2.matcher(s);
            String uid = m2.group(1);
            String pwd = m2.group(2);
            
            try {
                //pro SID vygeneruje nove DID
                NewDeviceReturnRecord newDevice = DC.newDevice(uid,pwd);
                if(newDevice==null){
                    //auth failed
                    this.result=FAIL;
                    return ParseResult.RESULT;
                }else{
                    //auth OK, posle DID, SID a OTP
                    this.result=OK+" DID "+newDevice.DID+" SID "+newDevice.SID+" OTP "+newDevice.OTP;
                    return ParseResult.RESULT;
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                this.result=ERR;
                return ParseResult.RESULT;
            }
        } else{
            this.result=ERR;
            return ParseResult.RESULT;
        }
        return null;
    }
    public synchronized String getResult(){
        return result;
    }
}
