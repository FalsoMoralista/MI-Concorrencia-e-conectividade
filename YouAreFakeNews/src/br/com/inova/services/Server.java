/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.services;

import br.com.inova.exception.NoAverageException;
import br.com.inova.model.Client;
import interfaces.Services;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Server implements Services {

    private Properties services = new Properties();
    private Properties NEWS_LIST = new Properties();

    private Client[] server_list;

    private static String SERVICE_NAME;
    private static int PORT;
    private String IP;

    public Server(String name) throws FileNotFoundException, IOException {
        services.load(new FileInputStream(new File("rmi/service_list/" + name + ".properties")));
        SERVICE_NAME = services.getProperty("SERVICE_NAME");
        PORT = Integer.parseInt(services.getProperty("PORT"));
        IP = services.getProperty("IP");
        System.setProperty("java.rmi.server." + SERVICE_NAME, IP);

        NEWS_LIST.load(new FileInputStream(new File("db/news_list.properties")));
    }

    /**
     * Run.
     *
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
     * Rate an news.
     *
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
            Path path = Paths.get(db.getPath());

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.write(String.valueOf(rate));
                writer.newLine();
            }

            db = new File("db/news/" + name + ".properties");
            db.createNewFile();
            path = Paths.get(db.getPath());

            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.write("NAME = " + name);
                writer.newLine();
                writer.write("DECIDED = FALSE");
                writer.newLine();
                writer.write("MEAN" + " = " + String.valueOf(rate));
                writer.newLine();
                writer.write("SD" + " = " + "0");
            }
        } else {
            Runnable r = () ->{
                startAgreement(newsID);
            };
            if(rate + sd(newsID) >= mean(newsID)){
                new Thread(r).start();
            }
            Path path = Paths.get(db.getPath());
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                writer.write(String.valueOf(rate));
                writer.newLine();
                writer.close();
            }
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(("db/news/" + name + ".properties"))));
            prop.setProperty("MEAN", String.valueOf(mean(newsID)));
            prop.store(new FileOutputStream(new File(("db/news/" + name + ".properties"))), "");
            prop.setProperty("SD", String.valueOf(sd(newsID)));
            prop.store(new FileOutputStream(new File(("db/news/" + name + ".properties"))), "");
        }

        System.out.println("Sucessfully");
    }

    private int mean(int newsID) throws IOException {
        List<String> lines = getNewsFileAsStream(newsID);
        int[] avg = new int[1];
        lines.forEach(line -> {            
            avg[0] += Integer.parseInt(line);
        });
        int mean = avg[0] / lines.size();
        return mean;
    }

    /**
     *  Calculates the standard deviation from a news mean. 
     */
    private int sd(int newsID) throws IOException{
        int[] sd = new int[1];
        int mean = mean(newsID);
        List<String> lines = getNewsFileAsStream(newsID);
        int[] num = new int[1];
        lines.forEach(line -> {
            num[0] = Integer.parseInt(line);
            sd[0] += Math.pow(num[0] - mean,2);
        });
        
        return (int) Math.sqrt(sd[0]/lines.size());
    }
    
    /**
     *  Return the news file as a stream. 
     */
    private List<String> getNewsFileAsStream(int newsID) throws IOException{
        String name = NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(newsID) + ']');
        File db = new File("db/news/" + name + ".txt");
        Path path = Paths.get(db.getPath());
        List<String> lines = Files.readAllLines(path);
        return lines;
    }
    
    /**
     * Return the truncated average from a news.
     *
     * @param newsID
     * @return
     * @throws java.rmi.RemoteException
     * @throws br.com.inova.exception.NoAverageException
     */
    @Override
    public int getTrunkAVG(int newsID) throws RemoteException, NoAverageException, IOException {
        System.out.println("Retrieving mean value for the news " + newsID);

        String name = NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(newsID) + ']');

        File db = new File("db/news/" + name + ".txt");

        if (!db.exists()) {
            throw new NoAverageException();
        }

        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(("db/news/" + name + ".properties"))));
        int val = Integer.parseInt(prop.getProperty("MEAN"));
        return val;
    }

    /**
     * Retrieve the available news.
     *
     * @return
     * @throws java.rmi.RemoteException
     */
    @Override
    public Properties getNews() throws RemoteException {
        return NEWS_LIST;
    }

    private void startAgreement(int newsID){
        System.out.println("Starting agreement");
    }

    public void connect() throws FileNotFoundException, FileNotFoundException, IOException, RemoteException, NotBoundException {
        server_list = new Client[services.size() - 1];
        int aux = 0;
        for (int i = 0; i < services.size(); i++) {
            String NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            if (!NAME.equals(SERVICE_NAME)) {
                server_list[aux++] = new Client(NAME);
            }
        }
    }

    public static void main(String[] args) throws IOException, RemoteException, AlreadyBoundException, FileNotFoundException, NotBoundException {
        Server server = new Server("server0");
        server.run();
    }
}
