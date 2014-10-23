/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import wimdserver.db.sync.model.DRDBDevice;
import wimdserver.db.sync.model.DRDBRecord;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DeviceRecordDB{

    private class CoordRec{
            public String coord;
            public Date received;
            public CoordRec(String c,Date r){
                this.coord=c;
                this.received=r;
            }
        }
    
    private class DeviceRecord{
        public String otp,salt;
        public List<CoordRec> crs;
        public DeviceRecord(String otp,String salt,List<CoordRec> coordRecs){
            this.otp=otp;
            this.salt=salt;
            this.crs=coordRecs;
        }
    }
    //struktura: hashmap podle did -> LSS podle date (predpoklad, chci posledni udaj, pridat novy udaj - tedy nejnovejsi v hlave)
    final ConcurrentHashMap<Integer,DeviceRecord> recs;
    public DeviceRecordDB(){
        recs=new ConcurrentHashMap<>();
    }
    
    public synchronized void setRecord(int did,String OTPhash,String salt,String coord,Date received){
        DeviceRecord dr;
        if(recs.containsKey(did)){
            dr = recs.get(did);
            if(dr.otp.equals(OTPhash)&&dr.salt.equals(salt)){
                dr.crs.add(new CoordRec(coord, received));
            }else throw new IllegalArgumentException("Hash or salt don't match record for DID");
        }else{
            CoordRec cr = new CoordRec(coord,received);
            LinkedList<CoordRec> ll = new LinkedList<>();
            ll.add(cr);
            dr = new DeviceRecord(OTPhash,salt,ll);
            recs.put(did, dr);
        }
    }
    
    public synchronized String getOTPHash(int did){
        return recs.get(did).otp;
    }
    
    public synchronized String getSalt(int did){
        return recs.get(did).salt;
    }
    
    public synchronized boolean hasRecordForDID(int did){
        return recs.containsKey(did);
    }
    
    public synchronized DRDBDevice[] getDeviceTable(){
        DRDBDevice[] table = new DRDBDevice[recs.size()];
        int i=0;
        for(Entry<Integer,DeviceRecord> e:recs.entrySet()){
            table[i++] = new DRDBDevice(""+e.getKey(),e.getValue().otp,e.getValue().salt);
        }
        return table;
    }
    
    public synchronized DRDBRecord[] getRecordTable(){
        int count = 0;
        DRDBRecord[] table = new DRDBRecord[recs.entrySet().stream().map((e) -> e.getValue().crs.size()).reduce(count, Integer::sum)];
        
        int i=0;
        for(Entry<Integer,DeviceRecord> e:recs.entrySet()){
            for(CoordRec c:e.getValue().crs){
                table[i++]=new DRDBRecord(""+e.getKey(),i%e.getValue().crs.size(),c.coord,c.received);
            }
        }
        
        return table;
    }
}
