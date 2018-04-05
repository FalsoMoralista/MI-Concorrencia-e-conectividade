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
    private static int IDS;

    public void registerNewClient(Client c) throws ClientAlreadyRegisteredException {
        try {
            Client retrieved = clients.get(c);
            retrieved.toString();
            throw new ClientAlreadyRegisteredException();
        }catch(NullPointerException ex){
            clients.put(c.toString(), c);
            server.attach(new WaterFlowMeasurer(Integer.toString(IDS++),c.toString()));
        }
    }
    
    public void removeClient(Client c) throws ClientAlreadyRemovedException{
        try {
            Client retrieved = clients.get(c);
            retrieved.toString();
            throw new ClientAlreadyRemovedException();
        }catch(NullPointerException ex){
            clients.remove(c.toString(), c);
            // todo verificar se o codigo abaixo para remover um observador funciona..
            Observer obs;
            for(int i = 0; i < IDS; i++){
                obs = server.getObserver("WaterFlowMeasurer{" + "id=" + i + ", associated=" + c.toString() + '}');
                server.detach(obs); 
            }
        }
    }
}
