/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain;

import it.appliweb.blockchain.data.DataBlock;

/**
 *
 * @author severino.cianciaruso@ennova.it
 */
public class Test {

    /**
     * @param args the command line arguments
     * "hash": "000006d1de58a1fa42165cf21d27667c84c230be06e4fa8c9854c6f83d246cf4",
    "previousHash": "000001ffa1a90711fb549006b201f5e34ff9c1b2b5e8053e1adb126b7edb8ff6",
    "data": "Hi im block-99",
    "timeStamp": 1543142075158,
    "nonce": 1996799
     */
    public static void main(String[] args) {
        DataBlock db = new DataBlock("test","Struct Title","Hi im block-99");
                
        Block mBlock = new Block(db,"000001ffa1a90711fb549006b201f5e34ff9c1b2b5e8053e1adb126b7edb8ff6");
//        mBlock.timeStamp=1543142075158l;
//        mBlock.nonce=1996799;
        System.out.println(mBlock.calculateHash());
    }
    
}
