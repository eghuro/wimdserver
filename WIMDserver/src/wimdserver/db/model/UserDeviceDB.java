/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.HashMap;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class UserDeviceDB {
    
    private final HashMap<String,Integer> SID2DID;
    
    public UserDeviceDB(){
        this.SID2DID=new HashMap<>();
    }
    
    public int GetDID(String SID){
        return SID2DID.get(SID);
    }
    
    public void SetRecord(String SID,int DID){
        SID2DID.put(SID, DID);
    }
}
