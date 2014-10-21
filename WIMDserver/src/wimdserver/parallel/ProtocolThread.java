/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.parallel;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import wimdserver.net.Communicator;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
 class ProtocolThread extends Thread {
    Socket socket;
    public ProtocolThread(Socket s){
        this.socket=s;
    }
    
    @Override
    public void run(){
        try{
            try{
                Communicator c = new Communicator(socket);
                c.communicate();
            }finally{
                    socket.close();
            }
        }catch(IOException | UnsupportedOperationException e){
            Logger.getLogger(ProtocolThread.class.getName()).log(Level.SEVERE, "Caught exception: {0}", e.getMessage());
        }catch(InterruptedException e){
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ProtocolThread.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                this.interrupt();
            }
        }finally{
            ThreadManager TM = ThreadManager.TM;
            synchronized(TM){
                TM.Remove(this);
            }
        }
    }
    
}
