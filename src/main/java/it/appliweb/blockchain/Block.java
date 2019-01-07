/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain;

import it.appliweb.blockchain.data.DataBlock;
import it.appliweb.blockchain.utility.StringUtil;
import java.util.Date;

/**
 *
 * @author severino.cianciaruso@ennova.it
 */
public class Block {

    public String hash;
    public String previousHash;
    public int nonce;
    
    private final DataBlock data; //our data will be a simple message.
    private final long timeStamp; //as number of milliseconds since 1/1/1970.
    
    /**
     * Selected Block
     * @param data
     * @param previousHash 
     */  
    public Block(DataBlock data, String previousHash) {
//        this.nonce = (int) (Math.random() * 999);
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();

        this.hash = this.calculateHash(); //Making sure we do this after we set the other values.
    }
    

    //Calculate new hash based on blocks contents
    public final String calculateHash() {
        String calculatedhash = StringUtil.applySha256(
                previousHash
                + Long.toString(timeStamp)
                + Integer.toString(nonce)
                + data
        );
        return calculatedhash;
    }

    
    public long getTimeStamp() {
        return timeStamp;
    }

    
    public DataBlock getData() {
        return data;
    }
    
    
}
