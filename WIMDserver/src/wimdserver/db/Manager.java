/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db;

import wimdserver.db.controller.AuthController;
import wimdserver.db.controller.DatabaseController;
import wimdserver.db.controller.RecordController;
import wimdserver.db.controller.Synchronizer;
import wimdserver.db.controller.UserDeviceController;
import wimdserver.db.model.AuthDB;
import wimdserver.db.model.DeviceRecordDB;
import wimdserver.db.model.UserDeviceDB;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class Manager {
    
    public static final Manager MANAGER = new Manager();
    public static Manager getInstance(){
        return MANAGER;
    }
    
    private final AuthDB AUTH;
    private final DeviceRecordDB DRDB;
    private final UserDeviceDB UDDB;
    
    private final DatabaseController DBC;
    
    private final Synchronizer[] SYNCERS;
    
    private Manager(){
        AUTH = new AuthDB();
        DRDB = new DeviceRecordDB();
        UDDB = new UserDeviceDB();

        AuthController ac=new AuthController(AUTH);
        DBC = new DatabaseController(
                ac,
                new RecordController(DRDB),
                new UserDeviceController(UDDB,ac)
            );
        
        SYNCERS = new Synchronizer[0];
    }
    
    public AuthDB getAuthDB(){
        return AUTH;
    }
    
    public DeviceRecordDB getDeviceRecordDB(){
        return DRDB;
    }
    
    public UserDeviceDB getUserDeviceDB(){
        return UDDB;
    }
    
    public DatabaseController getDatabaseController(){
        return DBC;
    }
    
    public Synchronizer[] getSynchronizers(){
        return SYNCERS;
    }
}
