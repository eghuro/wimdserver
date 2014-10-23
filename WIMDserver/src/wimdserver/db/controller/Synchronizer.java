/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.controller;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import wimdserver.db.model.AuthDB;
import wimdserver.db.model.DeviceRecordDB;
import wimdserver.db.model.UserDeviceDB;
import wimdserver.db.sync.drivers.IDriver;
import wimdserver.db.sync.model.AuthSession;
import wimdserver.db.sync.model.Row;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Synchronizer {
    
    
    public Synchronizer(){
        
    }
    
    public void synchronizeAuthDB(AuthDB ad,IDriver driver) throws ParseException {
        //AUTH SALT
        String[] keys=getKeys(ad.GetSaltTable(),driver,"AuthSalt");
        //zaznamy s temito klici chci vlozit do db
        for(String key:keys){
            ad.setUser(key, driver.getRowByKey("AuthSalt",key).getItem("hash"), driver.getRowByKey("AuthSalt",key).getItem("salt"));
        }

        //AUTH DB
        keys = getKeys(ad.GetSessionTable(),driver,"AuthSession");
        for(String key:keys){
            ad.registerSID(key, driver.getRowByKey("AuthSalt",key).getItem("uid"), AuthSession.getDateFormat().parse(driver.getRowByKey("AuthSalt", key).getItem("validity")));
        }
    }
    
    public void synchronizeDeviceRecordDB(DeviceRecordDB drdb,IDriver driver) throws ParseException{
        String[] k1 = getKeys(drdb.getDeviceTable(),driver,"DRDBDevice");
        String[] k2 = getKeys(drdb.getRecordTable(),driver,"DRDBRecord");
        for(String did:k1){//drdbdevice
            //naber recordy z drdbrecord
            String otp = driver.getRowByKey("DRDBDevice", "did").getItem("otp");
            String salt = driver.getRowByKey("DRDBDevice","did").getItem("salt");
            for(String id:k2){
                if(driver.getRowByKey("DRDBRecord", id).getItem("did").equals(did)){
                    String coord = driver.getRowByKey("DRDBRecord", id).getItem("coord");
                    String received = driver.getRowByKey("DRDBRecord",id).getItem("received");

                    drdb.setRecord(Integer.parseInt(did), otp, salt, coord, AuthSession.getDateFormat().parse(received));
                }
            }
        }
    }
    
    public void synchronizeUserDeviceDB(UserDeviceDB uddb,IDriver driver){
        String[] keys=getKeys(uddb.getTable(),driver,"UDDB");
        for(String key:keys){
            uddb.setRecord(Integer.parseInt(key), driver.getRowByKey("UDDB", key).getItem("uid"));
        }
    }
    
    private String[] getKeys(Row[] as,IDriver driver,String table){
        HashSet<String> primaries = new HashSet<>();
        //export
        for(Row a:as){//polozka v authdb
            Row b=driver.getRowByKey(table,a.getPrimary());//najdi odpovidajici zaznam v ulozisti
            if(b!=null){
                Iterator<String> ib=b.iterator();
                for(String x:a){//over hodnoty
                    if(ib.hasNext()){
                        if(!x.equals(ib.next())){//zaznamy se lisi - aktualizuj uloziste nasim zaznamem
                            driver.rmRow(table,a.getPrimary());
                            driver.setRow(table,a);
                            break;
                        }
                    } else throw new RuntimeException();//nekompadibilni format                }
                }
            }else{//novy zaznam
                driver.setRow(table,a);
            }
            primaries.add(a.getPrimary());
        }
        //nyni vim, ze co je v DB je i v ulozisti
        
        //import
        //chci z uloziste zaznamy s klici, ktere nejsou v primaries
        int x = driver.getKeys(table).length-primaries.size();
        String[] keys = new String[x];
        int i=0;
        for(String k:driver.getKeys(table)){
            if(!primaries.contains(k)){
                keys[i++]=k;
            }
        }
        return keys;
    }
}
