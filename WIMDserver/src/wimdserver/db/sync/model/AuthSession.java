/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class AuthSession extends Row {
    static{
        item_names=new String[]{"sid","uid","validity"};
    }
    
    public AuthSession(String sid,String uid,Date validity){
        SimpleDateFormat df = new SimpleDateFormat();
        
        items = new String[3];
        items[0] = sid;
        items[1] = uid;
        items[2] = df.format(validity);
    }
}
