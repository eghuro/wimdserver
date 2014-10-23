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
public class AuthSalt extends Row {
    static{//static initializer
        item_names=new String[]{"uid","hash","salt"};
        primary="uid";
    }
    
    public AuthSalt(String uid, String hash, String salt){
        this.items=new String[3];
        this.items[0]=uid;
        this.items[1]=hash;
        this.items[2]=salt;
    }
    
}
