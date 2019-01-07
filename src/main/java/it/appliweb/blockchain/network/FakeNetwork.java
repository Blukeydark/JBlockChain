/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain.network;

import it.appliweb.blockchain.Block;
import it.appliweb.blockchain.data.DataBlock;
import java.util.ArrayList;
import it.appliweb.blockchain.iface.PeerEvents;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author severino.cianciaruso@ennova.it
 */
public class FakeNetwork {

    /**
     * pubbliche condivisa da tutta la rete
     */
    // blockChain 
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    // difficoltà dell Proof Of Work
    public static int difficulty = 5;
    // hash target per minare i blocchi
    public static String hashTarget;
    // lunghezza massima della catena
    public static int maxBlock = 10;

    /**
     * private
     */
    private Block newBlock = null;
    private boolean blockTacked = false;

    // eventi verso tutti i peers
    private static PeerEvents callBackPeers;

    /**
     * Costruttore
     *
     * @param callBackPeers events to peers
     * @param difficulty enigma
     */
    public FakeNetwork(PeerEvents callBackPeers, int difficulty) {
        // seve = 73 65 76 65
        FakeNetwork.difficulty = difficulty;
        hashTarget = new String(new char[FakeNetwork.difficulty]).replace('\0', '0');
        FakeNetwork.callBackPeers = callBackPeers;
    }

    /**
     * BlockChain condivisa
     *
     * @return
     */
    public synchronized ArrayList<Block> getBlockchain() {
        return blockchain;
    }

    /**
     * Utility: true if chain > 0
     *
     * @return
     */
    public synchronized boolean isStartedChain() {
        return blockchain.size() > 0;
    }

    /**
     * Utility: true if max block
     *
     * @return
     */
    public synchronized boolean imFull() {
        if (blockchain.size() < maxBlock) {
            return false;
        }
        return true;
    }

    /**
     * Object.wait()! Only one block at time
     *
     * @param dataBlock
     */
    public synchronized void setNewBlock(DataBlock dataBlock) {
        if (null != this.newBlock) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FakeNetwork.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        String prevHash = this.getPrevHash();
        callBackPeers.onMsg("SET block-" + (blockchain.size()) + " prevHash-" + prevHash);
        this.newBlock = new Block(dataBlock, prevHash);
    }

    /**
     * Only one can take it at time
     *
     * @return
     */
    public synchronized Block getNewBlock() {
        Block tempReturn = null;
        if (blockTacked == true) {
            return null;
        }
        if (null != this.newBlock) {
            tempReturn = this.newBlock;
            blockTacked = true;
        }
        return tempReturn;
    }

    /**
     * check if valid add new block EVENTS: onMaxBlocks; onNewBlock notify all
     * threads
     *
     * @param newBlock
     * @return
     */
    public synchronized boolean putBlock(Block newBlock) {
        if (!this.isBlocValid(newBlock)) {
            return false;
        }

        blockchain.add(newBlock);

        if (blockchain.size() >= FakeNetwork.maxBlock) {
            callBackPeers.onMaxBlocks(blockchain);
        }

        callBackPeers.onNewBlock(newBlock);

        this.newBlock = null;
        blockTacked = false;
        notifyAll();

        return true;
    }

    public synchronized String getPrevHash() {
        String prevHash = 0 == blockchain.size() ? "0" : blockchain.get(blockchain.size() - 1).hash;
        return prevHash;
    }

    public synchronized static void printSituation(String msg) {
        callBackPeers.onMsg(msg + "\n");
    }

    public boolean isBlocValid(Block newBlock) {
        String nameBlock = newBlock.getData() + " ERROR: ";
        if (!newBlock.hash.equals(newBlock.calculateHash())) {
            printSituation(nameBlock + "Current Hashes not equal");
            return false;
        } else if (null == newBlock.previousHash || newBlock.previousHash.equals("")) {
            printSituation(nameBlock + "not previousHash");
            return false;
        } else if (this.isStartedChain() && newBlock.previousHash.equals("0")) {
            printSituation(nameBlock + "chain already started!");
            return false;
        } else if (!newBlock.previousHash.equals(this.getPrevHash())) {
            printSituation(nameBlock + "previousHash it's not valid");
            return false;
        } else if (blockchain.size() > 0 && newBlock.getTimeStamp() < blockchain.get(blockchain.size() - 1).getTimeStamp()) {
            printSituation(nameBlock + "timeStamp error");
            return false;
        }

        return true;
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                printSituation("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                printSituation("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                printSituation("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

}
