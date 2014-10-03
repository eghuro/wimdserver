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
    static final int STP=1234;//service thread port
    static final int SSP=6666;//server socket port
    static final int MAXC = 5;//max connections
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServiceThread st = new ServiceThread(STP);
            st.start();
            
            ServerSocket s = new ServerSocket(SSP);
            
            LinkedList<ProtocolThread> threads;
            threads = new LinkedList<>();
            
            try{
                boolean work;
                SynchronizedWorkFlag swf = SynchronizedWorkFlag.INSTANCE;
                synchronized(swf){
                    work=swf.GetWork();
                }
                while(work){
                    if(threads.size()<MAXC){
                        Socket sock = s.accept();
                        ProtocolThread pt = new ProtocolThread(sock);
                        threads.add(pt);
                        pt.start();
                        synchronized(swf){
                            work=swf.GetWork();
                        }
                    } else{
                        //pockej dokud nejake spojeni nezhebne
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
