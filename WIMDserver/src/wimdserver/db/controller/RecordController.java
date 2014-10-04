/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import wimdserver.db.model.DeviceRecordDB;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class RecordController {
    
    private final DeviceRecordDB drdb;
    
    public RecordController(DeviceRecordDB drdb){
        this.drdb=drdb;
    }
    
    public String SetRecord(int DID,String OTP,String coord,Date received) throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception{
        //Potreba pouzit session id - je spojeno s danym DID? TODO
        //TODO prvotni ziskani OTP
        if(drdb.HasRecordForDID(DID)){
            String storedHash = drdb.GetOTPHash(DID);
            String storedSalt = drdb.GetSalt(DID);
            String saltedOTP = storedSalt+OTP;
            byte[] hash = Hasher.getHash(1024, saltedOTP);
            if(new String(hash,Charset.forName("UTF-8")).equals(storedHash)){
                String newOTP="";//TODO
                String newSalt=Hasher.saltPwd(newOTP);
                String newSaltedOTP=newSalt+newOTP;
                byte[] newHash=Hasher.getHash(1024,newSaltedOTP);
                drdb.SetRecord(DID, new String(newHash,Charset.forName("UTF-8")), newSalt, coord, received);
                return newOTP;
            }else throw new Exception();//TODO spatne OTP
        } else{
            String newOTP="";
            String newSalt=Hasher.saltPwd(newOTP);
            String saltedOTP = newSalt+newOTP;
            byte[] newHash = Hasher.getHash(1024, saltedOTP);
            drdb.SetRecord(DID, new String(newHash,Charset.forName("UTF-8")), newSalt, coord, received);
            return newOTP;
        }
    }
}
