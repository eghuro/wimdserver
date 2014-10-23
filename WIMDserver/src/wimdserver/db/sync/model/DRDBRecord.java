/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.model;

import java.util.Date;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DRDBRecord extends Row{
    static{
        item_names=new String[]{"id","did","coord","received"};
        primary="id";
    }
    
    public DRDBRecord(String DID,int i,String coord,Date received){
        if(i<0)
            throw new IndexOutOfBoundsException("i:"+i);
        items = new String[3];
        items[0]=DID+i;
        items[1]=DID;
        items[2]=coord;
        items[3]=AuthSession.getDateFormat().format(received);
    }
}
