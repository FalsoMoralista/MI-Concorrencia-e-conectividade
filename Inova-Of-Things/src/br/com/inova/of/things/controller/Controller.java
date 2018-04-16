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
import br.com.inova.of.things.model.Observer;
import br.com.inova.of.things.model.Server;
import br.com.inova.of.things.model.WaterFlowMeasurer;
import java.util.HashMap;

/**
 *
 * @author luciano
 */
public class Controller {

    private String host;
    private int port;
    
    private HashMap<String, Client> clients;

    public Controller() {
    }
    
    public Controller(String serverHost, int port) {
        this.host = serverHost;
        this.port = port;
    }
        
    public void registerNewClient(Client c) throws ClientAlreadyRegisteredException {
        
    }
    
    public void removeClient(Client c) throws ClientAlreadyRemovedException{
    }
    
    public Client getClient(String key) throws ClientNotFoundException{
    }
    
    public WaterFlowMeasurer getClientMeasurer(String key) throws ClientMeasurerNotFoundException{
        return null;
    }
    
    public static void main(String[] args) throws ClientAlreadyRegisteredException, ClientAlreadyRemovedException{
    }
}
