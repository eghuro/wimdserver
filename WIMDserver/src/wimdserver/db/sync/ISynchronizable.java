/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync;

/**
 * ISynchronizable databaze se umi synchronizovat s ulozistem na disku
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public interface ISynchronizable {
    public DBRecord[] export();
    public void load(DBRecord[] data);
}
