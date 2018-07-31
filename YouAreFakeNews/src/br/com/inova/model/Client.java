/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.model;

import br.com.inova.exception.NoAverageException;
import interfaces.Services;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Client implements Services{

    private String IP;
    private int PORT;
    private String SERVICE_NAME;
    private Services services;

    public Client(String name) throws FileNotFoundException, IOException, RemoteException, NotBoundException {
        
        Properties config = new Properties();
        config.load(new FileInputStream(new File("rmi/service_list/"+name+".properties")));
        
        this.IP = config.getProperty("IP");
        this.PORT = Integer.parseInt(config.getProperty("PORT"));
        this.SERVICE_NAME = config.getProperty("SERVICE_NAME");        
        init();
    }
    
    /**
     * Start.
     */
    private void init() throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(IP, PORT);
        services = (Services) reg.lookup(SERVICE_NAME);
    }

    @Override
    public void rateNews(int newsID, int rate) throws IOException, RemoteException, Exception {
        services.rateNews(newsID, rate);
    }

    @Override
    public int getTrunkAVG(int newsID) throws RemoteException, NoAverageException, IOException {
        return services.getTrunkAVG(newsID);
    }

    @Override
    public Properties getNews() throws RemoteException {
        return services.getNews();
    }    
    
    public static void main(String[] args) throws IOException, FileNotFoundException, RemoteException, NotBoundException {
    }
}
