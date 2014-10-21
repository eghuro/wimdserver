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
class SessionIDFactory{
    private SessionIDFactory(){
        sid=0;
    }

    public static final SessionIDFactory INSTANCE = new SessionIDFactory();
    private int sid;

    public synchronized String getSessionID(){
        String newsid=Integer.toHexString(sid);//pracuje s unsigned -> po preteceni na ++ pokracuje dal
        sid++;
        return newsid;
    }
}
