/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthSession extends Row {
    static{
        item_names=new String[]{"sid","uid","validity"};
        primary="sid";
    }
    private static final SimpleDateFormat DF = new SimpleDateFormat();
    
    public static DateFormat getDateFormat(){
        return DF;
    }
    
    public AuthSession(String sid,String uid,Date validity){
       
        
        items = new String[3];
        items[0] = sid;
        items[1] = uid;
        items[2] = DF.format(validity);
    }
}
