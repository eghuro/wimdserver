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
    
    public boolean UserOwnsDevice(String UID,int DID){
        return uddb.GetUID(DID).equals(UID);
    }
    
    public boolean SessionOwnsDevice(String SID,int DID){
        return UserOwnsDevice(ac.GetSessionID(SID),DID);
    }
}
