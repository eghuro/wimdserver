/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.parallel;

import java.net.Socket;
import java.util.LinkedList;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
class ThreadManager {
    
    public static final ThreadManager TM = new ThreadManager();
    
    public static ThreadManager getThreadManager(){
        return TM;
    }
    
    private final int MAXC=5;
    
    private final ProtocolThread[] threads;
    int i;
    
    private ThreadManager(){
        this.threads = new ProtocolThread[MAXC];
        for(int j=0;j<MAXC;j++){
            threads[j]=null;
        }
        this.i=0;
    }
    
    public synchronized boolean GetAvailable(){
        return threads.length<MAXC;
    }
    
    public synchronized void Add(ProtocolThread pt){
        for(int j=0;j<MAXC;j++){
            if(threads[j]==null){
                threads[j]=pt;
                i++;
                break;
            }
        }
    }
    
    public synchronized void Remove(ProtocolThread pt){
        for(int j=0;j<MAXC;j++){
            if(threads[j]==pt){
                threads[j]=null;
                if((i--)<MAXC)
                    this.notifyAll();
            }
        }
    }
    
    public synchronized void New(Socket s){
        for(int j=0;j<MAXC;j++){
            if(threads[j]==null){
                ProtocolThread pt;
                pt = new ProtocolThread(s);
                threads[j]=pt;
                pt.start();
                break;
            }
        }
        
    }
    
    public synchronized void Stop(){
        for(ProtocolThread pt:threads){
            try {
                pt.join();
            } catch (InterruptedException ex) {
                TM.ForceStop();
            }
        }
    }
    
    public synchronized void ForceStop(){
        for(ProtocolThread pt:threads){
            pt.interrupt();
        }
    }
}
