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
import wimdserver.db.Manager;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Communicator {
    final Socket socket;
    final BufferedReader in;
    final PrintWriter out;
    final Parser parser;
    
    public Communicator(Socket s) throws IOException{
        this.socket=s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        this.parser = new Parser(Manager.MANAGER.getDatabaseController());
    }
    
    public synchronized void communicate() throws InterruptedException, IOException,UnsupportedOperationException{
        boolean work=true;
        StringBuilder sb=new StringBuilder();
        while(work){
            char c = (char)in.read();
            if(Character.isWhitespace(c)) {
                if(sb.length()>0){
                    ParseResult pr = parser.parse(sb.toString());
                    switch(pr){
                        case RESULT:
                            String res = parser.getResult();
                            if(res!=null)
                                out.write(res);
                            break;
                        case PARSE:break;//nedelej nic (cti dale) - ale chci vyjimku na cokoliv jineho pro osetreni chyb
                        case STOP:
                            work=false;
                            break;
                        default: throw new UnsupportedOperationException(pr.name());
                    }
                }//jinak jde o bily znak na zacatku .. vynechavam
            } else {
                sb.append(c);
            }
        }
    }
}
