/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.services;

import interfaces.Services;
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
import java.util.Properties;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements Services {

    private Properties services = new Properties();
    private Properties NEWS_LIST = new Properties();
    
    private static String SERVICE_NAME;
    private static int PORT;
    private String IP;
        
    public Server(String name) throws FileNotFoundException, IOException {
        services.load(new FileInputStream(new File("rmi/service_list/" + name + ".services")));
        SERVICE_NAME = services.getProperty("SERVICE_NAME");
        PORT = Integer.parseInt(services.getProperty("PORT"));
        IP = services.getProperty("IP");
        System.setProperty("java.rmi.server." + SERVICE_NAME, IP);                
        
        NEWS_LIST.load(new FileInputStream(new File("db/news_list.properties")));        
    }

    /**
     * Run.
     * @throws RemoteException
     * @throws AlreadyBoundException
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NotBoundException
     */
    public void run() throws RemoteException, AlreadyBoundException, FileNotFoundException, IOException, NotBoundException {
        Registry registry = LocateRegistry.createRegistry(PORT);

        Services stub = (Services) UnicastRemoteObject.exportObject(this, PORT);

        registry.bind(SERVICE_NAME, stub);

        System.out.println("Server " + '[' + SERVICE_NAME + ']' + " running");
    }
    
    /**
     *  Rate an news. 
     * @param newsID
     * @param rate
     * @throws java.io.IOException
     * @throws java.rmi.RemoteException
     */
    @Override
    public void rateNews(int newsID, int rate) throws IOException, RemoteException {
        System.out.println("Rating " + rate + " to the news " + newsID);

        String name = NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(newsID) + ']');

        File db = new File("db/news/" + name + ".txt");

        if (!db.exists()) {
            db.createNewFile();
        }

        Path path = Paths.get(db.getPath());

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            writer.write(String.valueOf(rate));
            writer.newLine();
        }
        System.out.println("Sucessfully");        
    }

    /**
     * Return the truncated average from a news.  
     * @param newsID
     * @return 
     * @throws java.rmi.RemoteException
     */
    @Override
    public int getTrunkAVG(int newsID) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Properties getNews() throws RemoteException {
        return NEWS_LIST;                
    }

}
