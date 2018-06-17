/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.service;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdcom.interfaces.IServices;
import sdcom.model.Product;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements IServices {

    private HashMap<Long, Product> products;

    public Server() throws RemoteException {
        System.out.println("[Server started]");
        products = new HashMap<>();
    }

    @Override
    public boolean sell(Product product) throws RemoteException {

        System.out.println("Selling product " + product);

        Product p = products.get(product.getID());
        products.remove(p, p.toString());

        System.out.println("ok");

        return true;
    }

    @Override
    public Product get(long ID) throws RemoteException {
        System.out.println("Returning product " + ID);
        return products.get(ID);
    }

    @Override
    public void add(Product product) throws RemoteException {
        System.out.println("Adding product " + product);
        products.put(product.getID(), product);
    }

    public static void main(String[] args) throws AlreadyBoundException {
        try {
            Server server = new Server();
            Registry registry =  LocateRegistry.createRegistry(10112);
            registry.bind("amazon", server);
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
