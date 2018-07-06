/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import sdcom.interfaces.IServices;
import sdcom.service.Server;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Client implements Serializable, IServices {

    private String IP;
    private int PORT;
    private String SERVICE_NAME;
    private IServices services;

    public Client(String pathToConfigFile) throws FileNotFoundException, IOException, RemoteException, NotBoundException {
        Properties config = new Properties();
        config.load(new FileInputStream(new File(pathToConfigFile)));
        this.IP = config.getProperty("IP");
        this.PORT = Integer.parseInt(config.getProperty("PORT"));
        this.SERVICE_NAME = config.getProperty("SERVICE_NAME");
        init();
    }

    private void init() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(IP, PORT);
        services = (IServices) reg.lookup(SERVICE_NAME);
    }

    @Override
    public boolean sell(Product product) throws RemoteException {
        return services.sell(product);
    }

    @Override
    public Product get(int ID) throws RemoteException {
        return services.get(ID);
    }

    @Override
    public void add(Product product) throws RemoteException {
        services.add(product);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, IOException {
        Client c = new Client("src/sdcom/view/resources/rmi.properties");
    }
}
