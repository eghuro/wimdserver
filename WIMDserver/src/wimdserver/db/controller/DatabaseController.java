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
    
    public class NewDeviceReturnRecord{
        public int DID;
        public String OTP,SID;
        public NewDeviceReturnRecord(int DID,String OTP,String SID){
            this.DID=DID;
            this.OTP=OTP;
            this.SID=SID;
        }
    }
    
    public NewDeviceReturnRecord NewDevice(String UID,String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(ac.AuthenticateUser(UID, pwd)){
            String SID = ac.GetSessionID(UID);
            int DID=-1;//generate DID
            udc.SetRecord(UID, DID);
            String OTP=null;//generate initial OTP
            return new NewDeviceReturnRecord(DID,OTP,SID);
        }else return null;
    }
    
    public String NewRecord(String SID,int did,String OTP,String coord) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        if(ac.IsAuthenticated(SID)){
            if(udc.SessionOwnsDevice(SID, did)){
                try{
                    return rc.SetRecord(did, OTP, coord, new Date());
                }catch(PasswordException pe){
                    return null;
                }
            } else return null;
        } else return null;
    }
}
