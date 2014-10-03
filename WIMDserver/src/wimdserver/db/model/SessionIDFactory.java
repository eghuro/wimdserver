/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.db.model;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class SessionIDFactory{
    private SessionIDFactory(){

    }

    public static SessionIDFactory INSTANCE = new SessionIDFactory();

    public String GetSessionID(){
        return "";
    }
}
