/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ProtocolThread extends Thread {
    final LinkedList<ProtocolThread> l;
    Socket socket; 
    int id;
    static int cnt=0;
    public ProtocolThread(Socket s,LinkedList<ProtocolThread> l){
        this.socket=s;
        this.id=cnt++;
        this.l=l;
    }
    
    @Override
    public void run(){
        try{
            try{
                
            }finally{
                    socket.close();
            }
        }catch(IOException e){
            
        }finally{
            synchronized(l){
                boolean r=l.remove(this);
                if(!r) Logger.getLogger(ProtocolThread.class.getName()).log(Level.SEVERE,("ProtocolThread not in list: "+id));
                l.notify();
            }
        }
    }
    
}
