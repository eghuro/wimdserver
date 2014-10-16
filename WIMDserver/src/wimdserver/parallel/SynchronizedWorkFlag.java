/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wimdserver.parallel;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class SynchronizedWorkFlag {
    private boolean work;
    
    private SynchronizedWorkFlag(){
        this.work=true;
    }
    
    public static final SynchronizedWorkFlag INSTANCE = new SynchronizedWorkFlag();
    
    public static SynchronizedWorkFlag GetInstance(){
        return SynchronizedWorkFlag.INSTANCE;
    }
    
    public synchronized boolean GetWork(){
        return this.work;
    }
    
    public synchronized void SetWork(boolean value){
        this.work=value;
    }
}
