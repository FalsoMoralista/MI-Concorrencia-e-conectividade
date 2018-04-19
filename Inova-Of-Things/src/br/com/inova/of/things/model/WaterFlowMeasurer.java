/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class WaterFlowMeasurer extends Observer implements Serializable, Runnable {

    private boolean listening;
    private static final int port = 1111;
    private static final int IDLE = 10000;

    private String id;
    private double waterConsumed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WaterFlowMeasurer(String id) {
        super();
        this.id = id;
    }

    public double getWaterConsumed() {
        return waterConsumed;
    }

    public void setWaterConsumed(double waterConsume) {
        this.waterConsumed = waterConsume;
    }

    public void addConsume(double val) {
        this.waterConsumed += val;
    }

    @Override
    public String toString() {
        return id;
    }

    @Deprecated
    public void startListen() {
        this.listening = true;
        this.listen();
    }

    @Deprecated
    public void stopListening() {
        this.listening = false;
    }

    @Deprecated
    private void listen() {
        DatagramSocket socket = null;
        while (listening) {
            try {
                socket = new DatagramSocket(this.port);
                byte[] incomingData = new byte[10240];
                System.out.println("client measurer listening to the port " + this.port);
                while (true) {
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    socket.receive(incomingPacket);
                    byte[] data = incomingPacket.getData();
                    ByteArrayInputStream in = new ByteArrayInputStream(data);
                    ObjectInputStream is = new ObjectInputStream(in);
                    RequestPackage request = null;
                    ResponsePackage response = null;
                    try {
                        request = (RequestPackage) is.readObject();
                        response = new ResponsePackage("OK", "measure", new ClientMeasure(id, LocalDateTime.now(), waterConsumed));
                        ClientServer.requestUDP(incomingPacket.getAddress().getHostName(), incomingPacket.getPort(), response);
                    } catch (ClassNotFoundException ex) {
                        response = new ResponsePackage("ERROR", ex.getMessage());
                        Logger.getLogger(WaterFlowMeasurer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    @Deprecated
    private void getRequest(RequestPackage request) {
        switch (request.getMETHOD()) {
            case "GET":
                switch (request.getOBJECT_TYPE()) {
                    case "measure":
                        this.update();
                        break;
                }
                break;
        }
    }

    /**
     * Send the server an message with the client actual consume.
     *
     * @see ClientMeasure
     */
    @Override
    public void update() {
        String host;
        //TODO GET HOSTNAME FROM KEYBOARD INPUT
        System.out.println("updating " + id + "...");
        ClientMeasure reading = new ClientMeasure(id, LocalDateTime.now(), waterConsumed);
        RequestPackage request = new RequestPackage("POST", "client measure", reading.getId()+"|"+reading.getTime()+"|"+reading.getReading());
        try {
            ClientServer.requestUDP("localhost", port, request);
        } catch (SocketException ex) {
            Logger.getLogger(WaterFlowMeasurer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(WaterFlowMeasurer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(IDLE);
                this.update();
            } catch (InterruptedException ex) {
                Logger.getLogger(WaterFlowMeasurer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
