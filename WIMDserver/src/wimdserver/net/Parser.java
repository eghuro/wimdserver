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
    
    final String SID="SID";
    final String OTP="OTP";
    final String DID="DID";
    final String COORD="COORD";
    
    final String ERR="ERR";//exception
    final String FAIL="FAIL";//login
    final String OK="OK";
    
    final DatabaseController DC;
    
    private enum State{
        START,REC,DEV,
        SID,SIDV,OTP,OTPV,DID,DIDV,COORD,COORDV,
        D_UID,D_PWD;
    }
    
    State state;
    String sid,otp,coord,uid,pwd;
    int did;
    
    public Parser(DatabaseController dc){
        this.DC=dc;
        this.state=State.START;
    }
    public synchronized ParseResult parse(String s){
        switch(state){
            case START:
                switch (s) {
                    case REC:
                        state=State.REC;
                        return ParseResult.PARSE;
                    case DEV:
                        state=State.DEV;
                        return ParseResult.PARSE;
                    default:
                        state=State.START;
                        result=FAIL;
                        return ParseResult.RESULT;
                }
            case REC:
                if(s.equals(SID)){
                    state=State.SID;
                    return ParseResult.PARSE;
                }else{
                    state=State.START;
                    result=FAIL;
                    return ParseResult.RESULT;
                }
            case SID:
                this.sid=s;
                state=State.SIDV;
                return ParseResult.PARSE;
            case SIDV:
                if(s.equals(OTP)){
                    state=State.OTP;
                    return ParseResult.PARSE;
                }else{
                    state=State.START;
                    result=FAIL;
                    return ParseResult.RESULT;
                }
            case OTP:
                this.otp=s;
                state=State.OTPV;
                return ParseResult.PARSE;
            case OTPV:
                if(s.equals(DID)){
                    state=State.DID;
                }else{
                    state=State.START;
                    result=FAIL;
                    return ParseResult.RESULT;
                }
            case DID:
                try{
                    this.did=Integer.parseInt(s);
                    state=State.DIDV;
                    return ParseResult.PARSE;
                }catch(NumberFormatException e){
                    state=State.START;
                    result=FAIL;
                    return ParseResult.RESULT;
                }
            case DIDV:
                if(s.equals(COORD)){
                    state=State.COORDV;
                    return ParseResult.PARSE;
                }else{
                    state=State.START;
                    result=FAIL;
                    return ParseResult.RESULT;
                }
            case COORDV:
                this.coord=s;
                try{
                    result=DC.newRecord(sid, did, otp, coord);
                    //CEKEJ
                    
                    //POSLI OTP
                    state=State.START;
                    if(result==null){
                        result=FAIL;
                    }
                    return ParseResult.RESULT;
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                    Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
                    state=State.START;
                    result=ERR;
                    return ParseResult.RESULT;
                }
            case DEV:
                state=State.D_UID;
                return ParseResult.PARSE;
            case D_UID:
                state=State.D_PWD;
                this.uid=s;
                return ParseResult.PARSE;
            case D_PWD:
                try{
                    NewDeviceReturnRecord ndr = DC.newDevice(uid, pwd);
                    if(ndr==null){
                        this.result=FAIL;
                        state=State.START;
                        return ParseResult.RESULT;
                    }else{
                        result = DID+" "+ndr.DID+" SID "+ndr.SID+" OTP "+ndr.OTP;
                        state=State.START;
                        return ParseResult.RESULT;
                    }
                }catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
                    state=State.START;
                    this.result=ERR;
                    return ParseResult.RESULT;
                }
            default:
                state=State.START;
                this.result=FAIL;
                return ParseResult.RESULT;
        }
    }
    public synchronized String getResult(){
        return result;
    }
}
