/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain.data;

import java.util.Date;

/**
 *
 * @author severino.cianciaruso@ennova.it
 */
public class DataBlock {
    String type;
    String title;
    String data;
    long timestamp;

    public DataBlock(String type, String title, String data) {
        
        this.timestamp =  new Date().getTime();
        Date ddd = new Date();
        ddd.setTime(this.timestamp);
        this.type = type;
        this.title = title + " " + ddd.toString();
        this.data = data;
    }
    
}
