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
    
    public synchronized boolean userOwnsDevice(String UID,int DID){
        synchronized (uddb){
            return uddb.getUID(DID).equals(UID);
        }
    }
    
    public synchronized boolean sessionOwnsDevice(String SID,int DID){
        synchronized(ac){
            return userOwnsDevice(ac.getSessionID(SID),DID);
        }
    }
    
    public synchronized void setRecord(String UID,int DID){
        synchronized(uddb){
            uddb.setRecord(DID, UID);
        }
    }
}
