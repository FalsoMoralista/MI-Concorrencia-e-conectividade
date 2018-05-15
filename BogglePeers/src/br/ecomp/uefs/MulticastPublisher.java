/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class will be used to communicate between peers.
 * @author Luciano Araujo Dourado Filho
 */
public class MulticastPublisher {

    private Peer peer;

    private DatagramSocket socket;

    private InetAddress group;

    private byte[] buffer;    
    
    private static final int PORT =  4446;

    public MulticastPublisher(Peer peer) throws SocketException, UnknownHostException {
        this.peer = peer;
        socket = new DatagramSocket();
        group = peer.getGroup().getGroupAddress();
    }
                
    public void multicast(Object message) throws IOException{
                
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ObjectOutputStream os = new ObjectOutputStream(outputStream);

        os.writeObject(message);

        buffer = outputStream.toByteArray();
        
        socket.send(new DatagramPacket(buffer, buffer.length, group,PORT));
    }
}
