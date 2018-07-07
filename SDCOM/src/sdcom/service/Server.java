/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdcom.interfaces.IServices;
import sdcom.model.Product;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements IServices {

    private HashMap<Integer, Product> products;
    private static String SERVICE_NAME;
    private static int PORT;
    private String DB;

    public Server() throws RemoteException {
        products = new HashMap<>();
        load();
    }

    public Server(String propertiesName) throws RemoteException, FileNotFoundException, IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("resources/" + propertiesName + ".properties")));

        SERVICE_NAME = properties.getProperty("SERVICE_NAME");
        PORT = Integer.parseInt(properties.getProperty("PORT"));
        DB = "db/" + SERVICE_NAME + ".db";
        products = new HashMap<>();
        load();
    }

    private void save() throws RemoteException, IOException {

        LinkedList<Product> listProducts = new LinkedList<>();

        Iterator it = products.values().iterator();
        
        File f = new File(DB);
        
        f.delete();
        f.createNewFile();

        Path path = Paths.get(DB);

        while (it.hasNext()) {
            Product product = (Product) it.next();
            try (BufferedWriter writer = Files.newBufferedWriter(path,StandardOpenOption.APPEND)) {
                writer.write(product.getID() + "|");
                writer.write(product.getName() + "|");
                writer.write(product.getQuantity() + "|");
                writer.write(product.getPrice() + "|");
                writer.newLine();
                writer.flush();
            }
        }
    }

    private void load() throws RemoteException {

        try {

            List<String> lines = Files.readAllLines(Paths.get(DB));

            lines.forEach(line -> {

                StringTokenizer token = new StringTokenizer(line, "|");

                while (token.hasMoreElements()) {
                    String id = token.nextToken();
                    String name = token.nextToken();
                    String quantity = token.nextToken();
                    String price = token.nextToken();
                    Product p = new Product(name, Integer.parseInt(id), Integer.parseInt(quantity), Double.parseDouble(price));
                    products.put(p.getID(), p);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized boolean sell(Product product) throws RemoteException {

        System.out.println("Selling product " + product);

        Product p = products.get(product.getID());
        p.setQuantity(p.getQuantity() - 1);
        System.out.println("Quantidade atual: " + p.getQuantity());
        products.remove(p, p.toString());

        System.out.println("ok");

        try {
            save();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public Product get(int ID) throws RemoteException {
        System.out.println("Returning product " + ID);
        return products.get(ID);
    }

    @Override
    public void add(Product product) throws RemoteException {
        System.out.println("Adding product " + product);
        products.put(product.getID(), product);
        try {
            save();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(PORT);

        IServices stub = (IServices) UnicastRemoteObject.exportObject(this, PORT);

        registry.bind(SERVICE_NAME, stub);

        System.out.println("Server " + '[' + SERVICE_NAME + ']' + " running");
    }

    public static void main(String[] args) throws AlreadyBoundException, IOException {
        try {
            Server server = new Server("SDCOM");
            server.run();
        } catch (RemoteException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
