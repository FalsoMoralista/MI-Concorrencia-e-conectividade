/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.view;

import br.com.inova.exception.NoAverageException;
import br.com.inova.model.Client;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Main {

    private static Scanner scanner;
    private Properties NEWS_LIST;
    private Client client;
    
    public Main(){
    }

    /**
     * Show the menu options.
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     */
    public void menu() throws IOException, FileNotFoundException, RemoteException, NotBoundException, NoAverageException {
        scanner = new Scanner(System.in);
        boolean op = true;
                
        while(!before()){            
        }
        
        while (op) {
            System.out.println("Menu");
            System.out.println("-------------------------------------");
            System.out.println("| 1 -    Rate a news     | 0 - exit  |");
            System.out.println("--------------------------------------");
            System.out.println("| 2 - Get a news average |           |");
            System.out.println("-------------------------------------");
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    System.out.println("Choose one of the options below:");
                    listNews();
                    int newsID = Integer.parseInt(scanner.nextLine());
                    System.out.println("inform the rating:");
                    int rate = Integer.parseInt(scanner.nextLine());
                    rateNews(newsID, rate);
                    break;
                case 2:
                    System.out.println("Choose one of the options below:");
                    listNews();
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println("Mean is : "+client.getTrunkAVG(id));
                    break;
                case 0:
                    op = false;
                    break;
            }
        }
        System.exit(0);
    }
    
    public boolean before() throws FileNotFoundException, IOException, RemoteException, NotBoundException{
        Properties services = new Properties();
        services.load(new FileInputStream(new File("rmi/services.properties")));
        System.out.println("Please inform the server name: ");
        String name = scanner.nextLine();
        boolean op = false;
        for(int aux = 0; aux < services.size(); aux++){
            if(services.get("SERVICE_NAME"+'['+Integer.toString(aux)+']').equals(name)){
                op = true;
            }
        }
        if(op){
            client = new Client(name);
        }
        NEWS_LIST = client.getNews();
        return op;
    }

    /**
     * Exhibit all the available news to rate.
     */
    public void listNews() {
        for (int i = 0; i < NEWS_LIST.size(); i++) {
            System.out.println("--------------");
            System.out.println("| " + i + "-" + NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(i) + ']') + " |");
        }
        System.out.println("--------------");
    }

    /**
     * Rate a news.
     *
     * @param newsID
     * @param rate
     * @throws java.io.IOException
     */
    public void rateNews(int newsID, int rate) throws IOException {
        client.rateNews(newsID, rate);
    }

    public static void main(String[] args) throws IOException, FileNotFoundException, RemoteException, NotBoundException, NoAverageException {
       Main main = new Main();
       main.menu();
    }
}
