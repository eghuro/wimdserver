/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.HashMap;
import java.util.Map.Entry;
import wimdserver.db.sync.model.UserDevice;

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
    
    public synchronized String getUID(int DID){
        return DIDUID.get(DID);
    }
    
    public synchronized void setRecord(int DID,String UID){
        this.DIDUID.put(DID, UID);
    }
    
    public synchronized UserDevice[] getTable(){
        UserDevice[] table = new UserDevice[DIDUID.size()];
        int i=0;
        for(Entry<Integer,String> e:DIDUID.entrySet()){
            table[i++]=new UserDevice(e.getKey()+"",e.getValue());
        }
        return table;
    }
}
