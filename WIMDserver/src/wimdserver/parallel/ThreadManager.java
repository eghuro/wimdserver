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
    
    private final LinkedList<ProtocolThread> threads;
    
    private ThreadManager(){
        this.threads = new LinkedList<>();
    }
    
    public synchronized boolean GetAvailable(){
        return threads.size()<MAXC;
    }
    
    public synchronized void Add(ProtocolThread pt){
        threads.add(pt);
    }
    
    public synchronized void Remove(ProtocolThread pt){
        threads.remove(pt);
        if(threads.size()<MAXC){
            this.notifyAll();
        }
    }
    
    public synchronized void New(Socket s){
        ProtocolThread pt;
        pt = new ProtocolThread(s);
        threads.add(pt);
        pt.start();
    }
    
    public synchronized void Stop(){
        threads.stream().forEach((ProtocolThread pt) -> {
            try {
                pt.join();
            } catch (InterruptedException ex) {
                TM.ForceStop();
            }
        });
    }
    
    public synchronized void ForceStop(){
        threads.stream().forEach((pt) -> {
            pt.interrupt();
        });
    }
}
