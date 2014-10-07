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
import java.nio.CharBuffer;

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
        this.parser = new Parser();
    }
    
    public synchronized void communicate() throws IOException,UnsupportedOperationException{//TODO
        boolean work=true;
        CharBuffer cb=null;//TODO
        while(work){
            char c = (char)in.read();
            if(c!=' ')
                cb.put(c);
            else{
                ParseResult pr = parser.parse(cb.position(0).toString());
                work = !pr.equals(ParseResult.STOP);
                switch(pr){
                    case PARSE:break;
                    case RESULT:
                        String res = parser.getResult();
                        if(res!=null)
                            out.write(res);
                        break;
                    case STOP:
                        work=false;
                        break;
                    default: throw new UnsupportedOperationException(pr.name());//TODO
                }
            }
        }
    }
}
