/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.controller;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class NewDeviceReturnRecord {
     public int DID;
    public String OTP,SID;
    public NewDeviceReturnRecord(int DID,String OTP,String SID){
        this.DID=DID;
        this.OTP=OTP;
        this.SID=SID;
    }
}
