/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.controller;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.exceptions.ClientMeasurerNotFoundException;
import br.com.inova.of.things.exceptions.ClientNotFoundException;
import br.com.inova.of.things.model.Client;
import br.com.inova.of.things.model.ClientServer;
import br.com.inova.of.things.model.WaterFlowMeasurer;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author luciano
 */
public class Controller {

    private static String host;
    private static int port;
    private static ClientServer cs = new ClientServer();

    private final String ADD = "1";        
    private final String DEL = "0";        
    private final String GET = "2";        
    
    public Controller() {
    }
    
    public Controller(String serverHost, int port) {
        this.host = serverHost;
        this.port = port;
    }
        
    public void registerNewClient(Client c) throws ClientAlreadyRegisteredException {
        String formatedMsg = ADD+"|"+c.getEmail()+"-"+c.getAddress()+":"+c.getZone()+";";
        ClientServer.sendTCP("localhost", 8888, formatedMsg);
    }
    
    public void removeClient(Client c) throws ClientAlreadyRemovedException{
    }
    
    public Client getClient(String key) throws ClientNotFoundException{
        return null;
    }
    
    public WaterFlowMeasurer getClientMeasurer(String key) throws ClientMeasurerNotFoundException{
        return null;
    }
    
    public static void main(String[] args) throws ClientAlreadyRegisteredException, ClientAlreadyRemovedException, IOException{
        ClientServer cs = new ClientServer();
        cs.sendTCP("localhost",8888,"hello" );
        cs.sendUDP("localhost", 1111, "udp");
    }
}
