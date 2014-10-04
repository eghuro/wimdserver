/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Communicator {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    
    public Communicator(Socket s) throws IOException{
        this.socket=s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
    }
    
    public void communicate() throws IOException{
        boolean work=true;
        while(work){
            String str = in.readLine();
            
        }
    }
}
