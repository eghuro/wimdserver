/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public class DBRecord {
    String table, name, value;
    
    public DBRecord(String table, String name, String value){
        this.table=table;
        this.name=name;
        this.value=value;
    }
    
    public void setTable(String table){
        this.table=table;
    }
    
    public void setName(String name){
        this.name=name;
    }
    
    public void setValue(String value){
        this.value=value;
    }
    
    public String getTable(){
        return table;
    }
    
    public String getName(){
        return name;
    }
    
    public String getValue(){
        return value;
    }
}
