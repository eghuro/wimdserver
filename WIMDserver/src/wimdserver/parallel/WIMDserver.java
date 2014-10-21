/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.parallel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class WIMDserver {
    static final int STP=1234;//service thread port
    static final int SSP=6666;//server socket port
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServiceThread st = new ServiceThread(STP);
            st.start();
            
            try(ServerSocket s = new ServerSocket(SSP)) {
                boolean work;
                SynchronizedWorkFlag swf = SynchronizedWorkFlag.INSTANCE;
                synchronized(swf){
                    work=swf.GetWork();
                }
                
                ThreadManager tm = ThreadManager.TM;
                
                while(work){
                    Socket sock = s.accept();
                    
                    synchronized(tm){
                        if(tm.GetAvailable()){
                            tm.New(sock);
                        }else{
                            try {
                                while(!tm.GetAvailable()){
                                    tm.wait();//ThreadManager available
                                }

                                synchronized(swf){//overit, zda mezitim nedoslo ke zmene swf
                                    work=swf.GetWork();
                                }
                                
                                if(work){
                                    tm.New(sock);//pouze pokud je i zde volno, otevri spojeni
                                }
                            } catch (InterruptedException ex) {
                                tm.ForceStop();
                                Logger.getLogger(WIMDserver.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    synchronized(swf){
                        work=swf.GetWork();
                    }
                }
                
            }finally{
                st.interrupt();
            }
        } catch (IOException ex) {
            Logger.getLogger(WIMDserver.class.getName()).log(Level.SEVERE, null, ex);
            ThreadManager tm = ThreadManager.TM;
            synchronized(tm){
                tm.ForceStop();
            }
        } catch (Exception e){
            Logger l = Logger.getLogger(WIMDserver.class.getName());
            l.log(Level.SEVERE,"Caught exception!",e);
            l.log(Level.INFO,"Stack trace",e.getStackTrace());
            for(Throwable t:e.getSuppressed()){
                l.log(Level.WARNING,"Suppressed",t);
            }
        }
        
    }
    
}
