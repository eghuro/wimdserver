/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.drivers;

import wimdserver.db.sync.DBRecord;

/**
 * Driver uloziste (MySQL, MongoDB, XML, textovy soubor, .....)
 * pozadovana fukncionalita: vlozit zaznam a nacist ulozene zaznamy
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public interface IDriver {
    boolean putRecord(DBRecord dbr);
    DBRecord[] getRecords();
}
