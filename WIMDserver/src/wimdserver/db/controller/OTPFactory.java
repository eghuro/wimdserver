/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.controller;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class OTPFactory {
    private OTPFactory(){
        
    }
    public static final OTPFactory INSTANCE = new OTPFactory();
    
    public synchronized String getNewOTP(){
        return null;
    }
}
