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
public class DRDBDevice extends Row {
    static{
        item_names=new String[]{"did","otp","salt"};
        primary="did";
    }
    
    public DRDBDevice(String DID,String OTP,String salt){
        items = new String[4];
        items[0]=DID;
        items[1]=OTP;
        items[2]=salt;
    }
}
