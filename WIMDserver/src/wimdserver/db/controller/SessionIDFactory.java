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
public class SessionIDFactory{
    private SessionIDFactory(){
        sid=0;
    }

    public static SessionIDFactory INSTANCE = new SessionIDFactory();
    private int sid;

    public String GetSessionID(){
        String newsid=Integer.toHexString(sid);
        sid++;
        return newsid;
    }
}