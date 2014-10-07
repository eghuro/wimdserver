/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.net;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Parser {
    private boolean work;
    public Parser(){
        work=true;
    }
    public synchronized String parse(String s){
        return "";
    }
    public synchronized boolean work(){
        return work;
    }
}
