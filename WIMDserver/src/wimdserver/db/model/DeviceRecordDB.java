/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import wimdserver.db.sync.DBRecord;
import wimdserver.db.sync.ISynchronizable;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DeviceRecordDB implements ISynchronizable {

    @Override
    public DBRecord[] export() {
        DBRecord[] dbr = new DBRecord[recs.size()*4];
        int i=0;
        for(Entry<Integer, LinkedList<DeviceRecord>> e:recs.entrySet()){
            LinkedList<DeviceRecord> ll = e.getValue();
            int j=0;
            for(DeviceRecord dr:ll){
                dbr[i]=new DBRecord("devR-otp",j+"",dr.otp);
                dbr[i+1]=new DBRecord("devR-coord",j+"",dr.coord);
                DateFormat df = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
                dbr[i+2]=new DBRecord("devR-received",j+"",df.format(dr.received));
                dbr[i+3]=new DBRecord("devR-salt",j+"",dr.salt);
                i=i+4;
                j++;
            }
            //e: otp coord received salt
        }
        return dbr;
    }

    @Override
    public void load(DBRecord[] data) {
        
    }
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
