/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.drivers;

import java.text.ParseException;
import wimdserver.db.sync.model.Row;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public interface IDriver{
    Row getRowByKey(String table,String s) throws ParseException;
    void rmRow(String table,String key);
    void setRow(String table,Row r);
    String[] getKeys(String table);
}
