/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DeviceRecordDB{

    private class DeviceRecord{
        public String otp,salt,coord;
        public Date received;
        //TODO LSS v device record, jedno aktualni OTP a salt pro DR
        public DeviceRecord(String otp,String salt,String coord, Date received){
            this.otp=otp;
            this.coord=coord;
            this.received=received;
            this.salt=salt;
        }
    }
    //struktura: hashmap podle did -> LSS podle date (predpoklad, chci posledni udaj, pridat novy udaj - tedy nejnovejsi v hlave)
    final ConcurrentHashMap<Integer,LinkedList<DeviceRecord>> recs;
    public DeviceRecordDB(){
        recs=new ConcurrentHashMap<>();
    }
    
    public synchronized void setRecord(int did,String OTPhash,String salt,String coord,Date received){
        //ulozit jen hash OTP
        DeviceRecord dr = new DeviceRecord(OTPhash,salt,coord,received);
        
        if(recs.containsKey(did)){
            LinkedList<DeviceRecord> ll = recs.get(did);
            ll.addFirst(dr);
        }else{
            LinkedList<DeviceRecord> ll = new LinkedList<>();
            ll.addFirst(dr);
            recs.put(did, ll);
        }
    }
    
    public synchronized String getOTPHash(int did){
        return recs.get(did).get(0).otp;
    }
    
    public synchronized String getSalt(int did){
        return recs.get(did).get(0).salt;
    }
    
    public synchronized boolean hasRecordForDID(int did){
        return recs.containsKey(did);
    }
}
