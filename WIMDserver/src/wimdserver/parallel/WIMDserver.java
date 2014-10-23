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
            
            SyncThread synt = new SyncThread();
            synt.start();
            
            try(ServerSocket s = new ServerSocket(SSP)) {
                boolean work;
                SynchronizedWorkFlag swf = SynchronizedWorkFlag.INSTANCE;
                synchronized(swf){
                    work=swf.GetWork();
                }
                
                while(work){
                    try(Socket sock = s.accept()){
                        while(!newT(sock)&&work){
                            ThreadManager tm = ThreadManager.TM;
                            synchronized(tm){
                                while(!tm.GetAvailable()){
                                    tm.wait();
                                }
                            }
                            synchronized(swf){
                                work=swf.GetWork();
                            }
                            if(!work){
                                sock.close();
                            }
                        }
                    }catch(IOException e){
                        log(e);
                    }finally{
                        synchronized(swf){
                            work=swf.GetWork();
                        }
                        continue;
                    }
                }
                
            }finally{
                st.interrupt();
                synt.interrupt();
            }
        } catch (IOException ex) {
            log(ex);
            ThreadManager tm = ThreadManager.TM;
            synchronized(tm){
                tm.ForceStop();
            }
        } catch (Exception e){
            log(e);
        }
        
    }
    
    static void log(Exception e){
        Logger l = Logger.getLogger(WIMDserver.class.getName());
        l.log(Level.SEVERE,"Caught exception!",e);
        l.log(Level.INFO,"Stack trace",e.getStackTrace());
        for(Throwable t:e.getSuppressed()){
            l.log(Level.WARNING,"Suppressed",t);
        }
    }
    
    static boolean newT(Socket sock){
        ThreadManager tm = ThreadManager.TM;
        synchronized(tm){
            if(tm.GetAvailable()){
                tm.New(sock);
                return true;
            }else{
                return false;
            }
        }
    }
    
}
