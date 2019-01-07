/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import it.appliweb.blockchain.data.DataBlock;
import it.appliweb.blockchain.network.FakeNetwork;
import it.appliweb.blockchain.network.Peer;
import static java.lang.Thread.yield;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.appliweb.blockchain.iface.PeerEvents;

/**
 *
 * @author severino.cianciaruso@ennova.it
 */
public class NoobChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static PeerEvents CALLBACK =null;
            
         
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        

        // Waiters
        CALLBACK= new PeerEvents(){
            
            @Override
            public void onMsg(Object msg) {            
                System.out.println(msg);
            }

            @Override
            public void onMaxBlocks(ArrayList<Block> mBlockchain) {    
                
                blockchain = mBlockchain;
                System.out.println("\nBlockchain is Valid: " + FakeNetwork.isChainValid());

                String blockchainJson = new GsonBuilder().setPrettyPrinting().create()
                        .toJson(blockchain);
                System.out.println("\nThe block chain: ");
                System.out.println(blockchainJson);  
                
                yield();
            }

            @Override
            public void onNewBlock(Block newBlock) {
                
                String blockchainJson = new GsonBuilder().setPrettyPrinting().create()
                        .toJson(newBlock);
                System.out.println("\nThe newBlock: ");
                System.out.println(blockchainJson);
                yield();
            }
        };        
        
        FakeNetwork mFakeNetwork = new FakeNetwork(CALLBACK,4);                
        
        
        int numTest = FakeNetwork.maxBlock;
        
        // Producer 10 peers che attendono blocchi da minare
        new Thread(()->{ 
            for(int x = 0; x < numTest; x++){    
                String data = "Hi im peer-"+ x;
                new Thread(new Peer(mFakeNetwork, FakeNetwork.difficulty, data)).start();
            }
        }).start();       
        

        new Thread(()->{ 
            for(int x = 0; x < numTest; x++){
                DataBlock db = new DataBlock("test","Struct Title","I Love You: "+ x);
                mFakeNetwork.setNewBlock(db);
            }
        }).start();
    }
}
