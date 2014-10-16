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
public class ServiceThread  extends Thread{
    final SynchronizedWorkFlag swf;
    ServerSocket st;
    public ServiceThread(int port) throws IOException{
        this.swf = SynchronizedWorkFlag.INSTANCE;
        st = new ServerSocket(port);
    }
    
    @Override
    public void run(){
        try {
            try(Socket s = st.accept()) {
                //cist vstup servisniho vlakna a poskytnout odp. vystup
                synchronized(swf){
                    swf.SetWork(false);
                }
                ThreadManager TM = ThreadManager.TM;
                synchronized(TM){
                    TM.Stop();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                st.close();
            } catch (IOException ex) {
                Logger.getLogger(ServiceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
