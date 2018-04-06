/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.controller;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.model.Client;
import br.com.inova.of.things.model.Observer;
import br.com.inova.of.things.model.Server;
import br.com.inova.of.things.model.WaterFlowMeasurer;
import java.util.HashMap;

/**
 *
 * @author luciano
 */
public class Controller {

    private Server server;
    private HashMap<String, Client> clients;

    public Controller() {
       this.server = new Server();
       this.clients = new HashMap<>();
    }
        
    public void registerNewClient(Client c) throws ClientAlreadyRegisteredException {
        try {
            Client retrieved = clients.get(c.toString());
            retrieved.toString();
            throw new ClientAlreadyRegisteredException();
        }catch(NullPointerException ex){
            clients.put(c.toString(), c);
            server.attach(new WaterFlowMeasurer(c.toString()));
        }
    }
    
    public void removeClient(Client c) throws ClientAlreadyRemovedException{
        try {
            Client retrieved = clients.get(c.toString());
            retrieved.toString();
            throw new ClientAlreadyRemovedException();
        }catch(NullPointerException ex){
            server.detach(server.getObserver(c.toString()));
            clients.remove(c.toString(), c);
        }
    }
    
    public Client getClient(String key){
        return clients.get(key);
    }
    
    public static void main(String[] args) throws ClientAlreadyRegisteredException{
        Controller c = new Controller();
        Client client = new Client("rua augusta 47","sul");
        c.registerNewClient(client);
        System.out.println(c.getClient(client.toString()));
        System.out.println(c.server.getObserver(client.toString()));
    }
}
