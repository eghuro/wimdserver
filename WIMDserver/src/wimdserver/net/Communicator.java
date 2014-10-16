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
import wimdserver.db.controller.AuthController;
import wimdserver.db.controller.DatabaseController;
import wimdserver.db.controller.RecordController;
import wimdserver.db.controller.UserDeviceController;
import wimdserver.db.model.AuthDB;
import wimdserver.db.model.DeviceRecordDB;
import wimdserver.db.model.UserDeviceDB;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Communicator {
    final Socket socket;
    final BufferedReader in;
    final PrintWriter out;
    final Parser parser;
    final DatabaseController dc;
    
    public Communicator(Socket s) throws IOException{
        this.socket=s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
        AuthController ac;
        ac = new AuthController(new AuthDB());
        this.dc=new DatabaseController(ac,new RecordController(new DeviceRecordDB()),new UserDeviceController(new UserDeviceDB(),ac));
        this.parser = new Parser(dc);
    }
    
    public synchronized void communicate() throws InterruptedException, IOException,UnsupportedOperationException{
        boolean work=true;
        StringBuilder sb=new StringBuilder();
        while(work){
            char c = (char)in.read();
            if(c!=' ')
                sb.append(c);
            else{
                ParseResult pr = parser.parse(sb.toString());
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
                    default: throw new UnsupportedOperationException(pr.name());
                }
            }
        }
    }
}
