/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class MulticastListener extends Thread{

    private MulticastSocket socket;
    
    private byte[] buffer;

    private Peer peer;
    
    private InetAddress group;
    
    private static final int PORT = 4446;
    
    public MulticastListener(Peer peer) throws IOException{
        this.peer = peer;
        this.group = peer.getGroup().getGroupAddress();
        socket = new MulticastSocket(PORT);        
        new Thread(this).start();
    }

    @Override
    public void run(){
        try {
            socket.joinGroup(group);
            byte[] incomingData = new byte[10240];
            while(true){
                DatagramPacket pack = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(pack);
            }
        } catch (IOException ex) {
            Logger.getLogger(MulticastListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}