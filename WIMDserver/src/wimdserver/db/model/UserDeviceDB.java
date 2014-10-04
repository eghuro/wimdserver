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
    //mapuji DID na UID - pro kazdy DID existuje prave jedno UID
    
    private final HashMap<Integer,String> DIDUID;
    
    public UserDeviceDB(){
        this.DIDUID=new HashMap<>();
    }
    
    public String GetUID(int DID){
        return DIDUID.get(DID);
    }
    
    public void SetRecord(int DID,String UID){
        this.DIDUID.put(DID, UID);
    }
}
