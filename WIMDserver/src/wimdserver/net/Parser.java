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
    String result;
    public Parser(){
        
    }
    public synchronized ParseResult parse(String s){
        return null;
    }
    public synchronized String getResult(){
        return result;
    }
}
