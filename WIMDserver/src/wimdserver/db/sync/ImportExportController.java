/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync;

import wimdserver.db.sync.drivers.IDriver;

/**
 * Ovladac pro nejaky IDriver - umi nacist data do ISynchronizable a ulozit data
 * z ISynchronizable databaze
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class ImportExportController {
    final IDriver DRIVER;

    public ImportExportController(IDriver driver){
        this.DRIVER=driver;
    }
    
    public void LoadData(ISynchronizable db){
        db.load(DRIVER.getRecords());
    }
    
    public void StoreData(ISynchronizable db){
        for(DBRecord dbr:db.export()){
            DRIVER.putRecord(dbr);
        }
    }
    
    public void Sync(ISynchronizable db){
        LoadData(db);
        StoreData(db);
    }
}
