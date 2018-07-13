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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import sdcom.interfaces.IServices;
import sdcom.model.Client;
import sdcom.model.Product;

/**
 * Represents a server.
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements IServices {

    private HashMap<String, HashMap<Integer, Product>> serverProducts;
    private static String SERVICE_NAME;
    private static int PORT;
    private String DB;
    private String IP;

    private Client[] server_list;

    private HashMap<Integer, Product>[] servers_products;
    private HashMap<Integer, Product> products;

    private Properties services = new Properties();

    public Server() throws RemoteException, IOException {
        services = new Properties();
        load();
    }

    public Server(String propertiesName) throws RemoteException, FileNotFoundException, IOException {
        services = new Properties();
        serverProducts = new HashMap<>();
        products = new HashMap<>();        
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File("resources/service_list/" + propertiesName + ".properties")));

        SERVICE_NAME = properties.getProperty("SERVICE_NAME");
        PORT = Integer.parseInt(properties.getProperty("PORT"));
        IP = properties.getProperty("IP");
        DB = "db/" + SERVICE_NAME + ".db";

        services.load(new FileInputStream(new File("resources/services.properties")));

        for (int i = 0; i < services.size(); i++) {
            serverProducts.put(services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']'), new HashMap<>());
        }

        System.setProperty("java.rmi.server." + SERVICE_NAME, IP);
        load();
    }

    /**
     * Save the current state.
     */
    private void save() throws RemoteException, IOException {

        for (int i = 0; i < services.size(); i++) {
            String NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            HashMap<Integer, Product> prds = serverProducts.get(NAME);
            String db = "db/" + NAME + ".db";
            Iterator it = serverProducts.values().iterator();
            File f = new File(db);

            f.delete();
            f.createNewFile();

            Path path = Paths.get(db);

            while (it.hasNext()) {
                Product product = (Product) it.next();
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                    writer.write(product.getID() + "|");
                    writer.write(product.getName() + "|");
                    writer.write(product.getQuantity() + "|");
                    writer.write(product.getPrice() + "|");
                    writer.newLine();
                    writer.flush();
                }
            }

        }
    }

    /**
     * Loads the current state.
     */
    private void load() throws RemoteException, IOException {

        for (int i = 0; i < services.size(); i++) {
            String NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            HashMap<Integer, Product> prds = serverProducts.get(NAME);
            String db = "db/" + NAME + ".db";
            List<String> lines = Files.readAllLines(Paths.get(db));
            lines.forEach(line -> {

                StringTokenizer token = new StringTokenizer(line, "|");

                while (token.hasMoreElements()) {
                    String id = token.nextToken();
                    String name = token.nextToken();
                    String quantity = token.nextToken();
                    String price = token.nextToken();
                    Product p = new Product(name, Integer.parseInt(id), Integer.parseInt(quantity), Double.parseDouble(price));
                    prds.put(p.getID(), p);
                }
            });
        }
        products = serverProducts.get(SERVICE_NAME);
    }

    /**
     * Sell a product. First check if the current server has the product in
     * stock, then ask to the other servers if can sell the product, if so, sell
     * it, other- wise don't sell.
     *
     * @param product
     * @return
     * @throws java.rmi.RemoteException
     */
    @Override
    public synchronized boolean sell(Product product) throws RemoteException {

        System.out.println("Trying to sell product " + product + " by ->" + '[' + SERVICE_NAME + ']');
        try {
            connect();
        } catch (IOException | NotBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (get(product.getID()).getQuantity() >= 1) {
            boolean op = true;
            for (int i = 0; i < server_list.length; i++) {
                op = server_list[i].canSell(product);
            }
            if (op) {
                decrease(product);
                try {
                    save();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("ok");
                return true;
            } else {
                System.out.println("Product could not be sold, sorry.");
                return false;
            }
        }
        return false;
    }

    private void decrease(Product p) {
        for (int i = 0; i < services.size(); i++) {
            String NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            HashMap<Integer, Product> prds = serverProducts.get(NAME);
            String db = "db/" + NAME + ".db";
            Product prod = prds.get(p.getID());
            prod.setQuantity(p.getQuantity() - 1);
        }

    }

    /**
     * Returns whether a product can be sold or not.
     *
     * @param product
     * @return
     * @throws java.rmi.RemoteException
     */
    @Override
    public boolean canSell(Product product) throws RemoteException {
        System.out.println("Can " + SERVICE_NAME + " sell the product " + product + "???");
        Product check = get(product.getID());
        if (check == null) {
            System.out.println("Yes(i don't have)");
            return true;
        }
        boolean sell = check.getQuantity() >= 1;
        if (sell) {
            System.out.println("Yes");
            Product p = products.get(product.getID());
            p.setQuantity(p.getQuantity() - 1);

            try {
                save();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            System.out.println("No");
        }
        return sell;
    }

    /**
     * Return a product given its ID.
     *
     * @param ID
     * @return
     * @throws RemoteException
     */
    @Override
    public Product get(int ID) throws RemoteException {
        System.out.println("Returning product " + ID + " ->" + '[' + SERVICE_NAME + ']');
        return products.get(ID);
    }

    /**
     *
     * @param product
     * @throws RemoteException
     */
    @Override
    public void add(Product product) throws RemoteException {
        System.out.println("Adding product " + product + " ->" + '[' + SERVICE_NAME + ']');
        products.put(product.getID(), product);
        try {
            save();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NotBoundException
     */
    public void run() throws RemoteException, AlreadyBoundException, FileNotFoundException, IOException, NotBoundException {
        Registry registry = LocateRegistry.createRegistry(PORT);

        IServices stub = (IServices) UnicastRemoteObject.exportObject(this, PORT);

        registry.bind(SERVICE_NAME, stub);

        System.out.println("Server " + '[' + SERVICE_NAME + ']' + " running");
    }

    public void connect() throws FileNotFoundException, FileNotFoundException, IOException, RemoteException, NotBoundException {
        server_list = new Client[services.size() - 1];
        for (int i = 0; i < services.size(); i++) {
            String NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            if (!NAME.equals(SERVICE_NAME)) {
                server_list[i] = new Client(NAME);
            }
        }

    }

    public static void main(String[] args) throws AlreadyBoundException, IOException {
        try {
            Server server = new Server("AMAZONIA");
            server.run();
        } catch (RemoteException | FileNotFoundException | NotBoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
