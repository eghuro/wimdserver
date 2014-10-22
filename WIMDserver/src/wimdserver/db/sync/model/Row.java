/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wimdserver.db.sync.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Alexander Mansurov <alexander.mansurov@gmail.com>
 */
public abstract class Row implements Iterable{
    static String[] item_names; //staticke pole s nazvy polozek - spolecne pro vsechny radky
    
    String[] items;//kazdy radek ma pole svych hodnot
    
    public String GetItem(String name){
        for(int i=0;i<item_names.length;i++){
            if(item_names[i].equals(name)){
                return items[i];
            }
        }
        throw new NoSuchElementException(name);
    }
    
    public String[] GetColumnNames(){
        return item_names;
    }
    
    public String[] GetColumnValues(){
        return items;
    }

    @Override
    public Iterator iterator() {
        return new Iterator(){
            int i=0;
            
            @Override
            public boolean hasNext() {
                return i<items.length;
            }

            @Override
            public Object next() {
                if(i<items.length){
                    return items[i++];
                }else{
                    throw new NoSuchElementException();
                }
            }
            
        };
    }
}
