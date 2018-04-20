/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.controller;

import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.exceptions.ClientMeasurerNotFoundException;
import br.com.inova.of.things.exceptions.ClientNotFoundException;
import br.com.inova.of.things.model.Client;
import br.com.inova.of.things.model.ClientRecord;
import br.com.inova.of.things.model.ClientServer;
import br.com.inova.of.things.model.RequestPackage;
import br.com.inova.of.things.model.ResponsePackage;
import br.com.inova.of.things.model.WaterFlowMeasurer;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class Controller {

    private static String host;
    private static int port = 8888;
    private static ClientServer cs = new ClientServer();

    private final String POST = "POST";
    private final String DEL = "DELETE";
    private final String GET = "GET";

    public Controller() {
    }

    public Controller(String serverHost) {
        this.host = serverHost;
    }
    

    public void registerNewClient(Client c) throws IOException, UnknownHostException, ClassNotFoundException {
        String clInfo = "|" + c.getEmail() + "|" + c.getAddress() + "|" + c.getZone();
        RequestPackage pack = new RequestPackage(POST, "client", clInfo);
        Object request = ClientServer.request(host, port, pack);
    }

    public void removeClient(String key) throws ClientAlreadyRemovedException, IOException, UnknownHostException, ClassNotFoundException {
        RequestPackage pack = new RequestPackage(DEL, "client", key);
        Object request = ClientServer.request(host, port, pack);
    }

    public Client getClient(String key) throws IOException, ClientNotFoundException, UnknownHostException, ClassNotFoundException {
        RequestPackage pack = new RequestPackage(GET, "client", key);
        Object request = ClientServer.request(host, port, pack);
        ResponsePackage response = (ResponsePackage) request;
        try {
            Client c = (Client) response.getRESPONSE_OBJ();
            c.toString();
            return c;
        } catch (NullPointerException ex) {
            throw new ClientNotFoundException();
        }
    }

    public WaterFlowMeasurer getClientMeasurer(String key) throws ClientMeasurerNotFoundException, IOException, UnknownHostException, ClassNotFoundException {
        RequestPackage pack;
        try {
            pack = new RequestPackage(GET, "measurer", this.getClient(key).toString());
            Object request = ClientServer.request(host, port, pack);
            ResponsePackage response = (ResponsePackage) request;
            try {
                WaterFlowMeasurer w = (WaterFlowMeasurer) response.getRESPONSE_OBJ();
                w.toString();
                return w;
            } catch (NullPointerException ex) {
                throw new ClientMeasurerNotFoundException();
            }
        } catch (ClientNotFoundException ex) {
            throw new ClientMeasurerNotFoundException();
        }
    }
    
    public ClientRecord getClientRecord(String key){
        return null;
    }

    public static void main(String[] args) {
        try {
            Controller cont = new Controller("localhost");
            cont.registerNewClient(new Client("lucianoadfilho@gmail.com", "rua sao caetano 57", "sul"));
            System.out.println(cont.getClientMeasurer("lucianoadfilho@gmail.com").getWaterConsumed());
        } catch (NullPointerException ex) {
            System.out.println("cliente nao encontrado");
            ex.printStackTrace();
        } catch (IOException | ClassNotFoundException | ClientMeasurerNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
