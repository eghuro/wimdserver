/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

import wimdserver.db.model.UserDeviceDB;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class UserDeviceController {
    private final UserDeviceDB uddb;
    private final AuthController ac;
    
    public UserDeviceController(UserDeviceDB uddb,AuthController ac){
        this.uddb=uddb;
        this.ac=ac;
    }
    
    public boolean userOwnsDevice(String UID,int DID){
        return uddb.getUID(DID).equals(UID);
    }
    
    public boolean sessionOwnsDevice(String SID,int DID){
        return userOwnsDevice(ac.getSessionID(SID),DID);
    }
    
    public void setRecord(String UID,int DID){
        uddb.setRecord(DID, UID);
    }
}
