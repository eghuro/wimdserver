/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class WIMDserver {
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServiceThread st = new ServiceThread(1234);
            st.start();
            
            ServerSocket s = new ServerSocket(6666);
            
            LinkedList<ProtocolThread> threads;
            threads = new LinkedList<>();
            
            try{
                boolean work;
                SynchronizedWorkFlag swf = SynchronizedWorkFlag.INSTANCE;
                synchronized(swf){
                    work=swf.GetWork();
                }
                while(work){
                    Socket sock = s.accept();
                    ProtocolThread pt = new ProtocolThread(sock);
                    threads.add(pt);
                    pt.start();
                    synchronized(swf){
                        work=swf.GetWork();
                    }
                }
                try{
                    for(ProtocolThread pt:threads){
                        pt.join();
                    }
                }catch(InterruptedException e){
                    threads.stream().forEach((pt) -> {
                        pt.interrupt();
                    });
                }
            } finally{
                s.close();
                st.interrupt();
            }
        } catch (IOException ex) {
            Logger.getLogger(WIMDserver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
