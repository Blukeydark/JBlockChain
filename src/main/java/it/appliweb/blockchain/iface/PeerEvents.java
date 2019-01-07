/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.appliweb.blockchain.iface;

import it.appliweb.blockchain.Block;
import java.util.ArrayList;

/**
 * Events Peers
 *
 * @author severino.cianciaruso@ennova.it
 */
public interface PeerEvents {
    public void onMsg(Object msg);
    public void onMaxBlocks(ArrayList<Block> blockchain);
    public void onNewBlock(Block newBlock);
}
