/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DatabaseController {
    final AuthController ac;
    final RecordController rc;
    final UserDeviceController udc;
    
    public DatabaseController(AuthController ac,RecordController rc,UserDeviceController udc){
        this.ac=ac;
        this.rc=rc;
        this.udc=udc;
    }
    
    public synchronized NewDeviceReturnRecord newDevice(String UID,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(ac.authenticateUser(UID, pwd)){
            String SID = ac.getSessionID(UID);
            int DID=-1;//generate DID
            synchronized(udc){
                udc.setRecord(UID, DID);
            }
            String OTP=null;//generate initial OTP
            return new NewDeviceReturnRecord(DID,OTP,SID);
        }else return null;
    }
    
    public synchronized String newRecord(String SID,int did,String OTP,String coord) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(ac.isAuthenticated(SID)){
            synchronized(udc){
                if(udc.sessionOwnsDevice(SID, did)){
                    try{
                        synchronized(rc){
                            return rc.setRecord(did, OTP, coord, new Date());
                        }
                    }catch(PasswordException pe){
                        return null;
                    }
                } else return null;
            }
        } else return null;
    }
}
