/*

 */
package it.appliweb.blockchain.network;

import it.appliweb.blockchain.Block;

/**
 *
 * @author severino.cianciaruso@gmail.com
 */
public class Peer  implements Runnable {

    int difficulty = 5;
    Block myBlock=null;
    FakeNetwork mFakeNetwork;
    Object data;    
    int pointPOW = 0; // punti della gara proof of woork
    
    public Peer(FakeNetwork mFakeNetwork, int difficulty, Object data) {
        this.difficulty=difficulty;
        this.mFakeNetwork=mFakeNetwork;
        this.data=data;
    }
    
    @Override
    public void run() {
        System.out.println(this.data + " : Started.");
        // finché la rete non è piena
        while(!mFakeNetwork.imFull()){
            // se ho un blocco da minare
            Block tempBlock = mFakeNetwork.getNewBlock();
            if(null != tempBlock)
            {
                myBlock = tempBlock;
                // tento il mine
                boolean mined = this.mineBlock(difficulty);
                
                if(mined && !mFakeNetwork.putBlock(myBlock))
                    FakeNetwork.printSituation(this.data + " : get error.");
            }            
        }
        FakeNetwork.printSituation(this.data + " : I mined "+this.pointPOW+" blocks!");
    }
    
    public boolean mineBlock(int difficulty) {
        while (!myBlock.hash.substring(0, difficulty).equals(FakeNetwork.hashTarget)) {
            myBlock.nonce++;
            myBlock.hash = myBlock.calculateHash();
        }
        this.pointPOW++;
        return true;
    }
}
