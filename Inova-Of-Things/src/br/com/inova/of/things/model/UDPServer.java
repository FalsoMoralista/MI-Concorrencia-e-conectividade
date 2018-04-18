/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.interfaces.IMyServer;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class UDPServer implements Runnable,IMyServer{

    private int PORT = 0;
    private boolean listening;
    private DatagramSocket socket;
    private static final int BUFF_SIZE = 1024;
    private byte[] buffer = new byte[BUFF_SIZE];
    private Server server;
    
    public UDPServer(int port, Server server) throws SocketException {
        this.server = server;
        this.PORT = port;
        socket = new DatagramSocket(PORT);
        this.startListen();
        new Thread(this).start();
    }

    @Override
    public void startListen() {
        this.listening = true;
    }

    @Override
    public void stopListening() {
        this.listening = false;
    }

    @Override
    public void run() {
        System.out.println("Server.started listening to the port -> " + PORT+" via UDP...");
        while (listening) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData());
                System.out.println("server.read -> " + received);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = null;
                String answer = "ok";
                packet = new DatagramPacket(answer.getBytes(), answer.getBytes().length, address, port);
                socket.send(packet);
            } catch (IOException ex) {
                Logger.getLogger(UDPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
