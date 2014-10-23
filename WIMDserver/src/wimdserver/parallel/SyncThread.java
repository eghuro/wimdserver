/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.parallel;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import wimdserver.db.Manager;
import wimdserver.db.controller.Synchronizer;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class SyncThread extends Thread {
    final long WAIT=1800000;//30 min TODO zmenit na 35 aby se rozhodil cykus s Parserem
    @Override
    public void run(){
        SynchronizedWorkFlag swf = SynchronizedWorkFlag.INSTANCE;
        Manager m = Manager.MANAGER;
        boolean work;
        synchronized(swf){
            work=swf.GetWork();
        }
        while(work){
            try{
                for(Synchronizer s:m.getSynchronizers()){
                    s.synchronizeAuthDB(m.getAuthDB());
                    s.synchronizeDeviceRecordDB(m.getDeviceRecordDB());
                    s.synchronizeUserDeviceDB(m.getUserDeviceDB());
                }
            }catch(ParseException e){
                Logger.getLogger(SyncThread.class.getName()).log(Level.SEVERE,null,e);
            }
            synchronized(swf){
                work=swf.GetWork();
            }
            if(work){
                try {
                    sleep(WAIT);
                } catch (InterruptedException ex) {
                    this.interrupt();
                }
            }
        }
    }
}
