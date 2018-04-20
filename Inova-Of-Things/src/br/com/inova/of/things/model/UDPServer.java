/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.interfaces.IMyServer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class UDPServer implements Runnable, IMyServer {

    private int PORT;
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
        System.out.println("Server.started listening to the port -> " + PORT + " via UDP...");
        try {
            byte[] incomingData = new byte[10240];
            while (listening) {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                RequestPackage request = null;
                try {
                    this.getRequest((RequestPackage) is.readObject());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(WaterFlowMeasurer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    private void getRequest(RequestPackage requestPackage) {
        switch (requestPackage.getMETHOD()) {
            case "POST":
                switch (requestPackage.getOBJECT_TYPE()) {
                    case "client measure":
                        LinkedList<String> parse = this.parse(requestPackage.getCONTENT());
                        System.out.println(requestPackage.getCONTENT());
                        server.write(parse.get(0),new ClientMeasure(parse.get(0), LocalDateTime.parse(parse.get(1)),Double.parseDouble(parse.get(2))));
                        break;
                }
                break;
        }
    }
    
    private LinkedList<String> parse(String str){
        LinkedList<String> parsed = new LinkedList<>();
        StringTokenizer token =  new StringTokenizer(str,"|");
        while(token.hasMoreTokens()){
            parsed.add(token.nextToken());
        }
        return parsed;
    }
    
}
