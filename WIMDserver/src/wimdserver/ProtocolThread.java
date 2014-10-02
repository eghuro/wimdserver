/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ProtocolThread extends Thread {
    Socket socket;
    int id;
    static int cnt=0;
    public ProtocolThread(Socket s){
        this.socket=s;
        this.id=cnt++;
    }
    
    @Override
    public void run(){
        try{
            try{
                
            }finally{
                    socket.close();
            }
        }catch(IOException e){
            
        }
    }
    
}
