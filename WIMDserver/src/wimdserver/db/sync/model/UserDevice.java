/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.model;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class UserDevice extends Row{
    static{
        item_names=new String[]{"did","uid"};
        primary="did";
    }
    
    public UserDevice(String did,String uid){
        items = new String[2];
        items[0]=did;
        items[1]=uid;
    }
}
