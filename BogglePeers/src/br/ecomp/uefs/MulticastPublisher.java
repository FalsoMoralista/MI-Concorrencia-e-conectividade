/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class will be used to communicate between peers.
 * @author Luciano Araujo Dourado Filho
 */
public class MulticastPublisher {

    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buffer;    
    private Peer peer;

    public MulticastPublisher(Peer peer) throws SocketException, UnknownHostException {
        this.peer = peer;
        socket = new DatagramSocket();
        group = InetAddress.getByName(peer.getGroup().getGroupAddress().toString());
    }
                
    public void multicast(Object message){
    }
}
