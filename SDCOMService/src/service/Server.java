/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.HashMap;
import library.interfaces.IServices;
import library.model.Product;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements IServices {

    private HashMap<Long, Product> products;

    public Server() {
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

    public static void main(String[] args) {
        try {
            Server server = new Server();
            Naming.rebind("sdcom", server);
        } catch (MalformedURLException | RemoteException e) {
            System.out.println("Server errors: " + e.getMessage());
        }
    }
}
