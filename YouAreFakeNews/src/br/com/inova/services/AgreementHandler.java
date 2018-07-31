/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import org.jgroups.Message;
import br.com.inova.model.Package;
import br.com.inova.model.Vote;
/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class AgreementHandler {

    private final int NEWS_ID;
    private AgreementProtocolManager manager;
    private ConnectionHandler connectionHandler;
    private Properties news;
    private String news_name;
    private int amount_nodes;

    public AgreementHandler(int NEWS_ID, ConnectionHandler handler, int amount_nodes) throws FileNotFoundException, IOException, Exception {
        this.NEWS_ID = NEWS_ID;
        manager = new AgreementProtocolManager();
        this.connectionHandler = handler;
        news_name = connectionHandler.getServer().getNews().getProperty("NEWS_NAME" + '[' + NEWS_ID + ']');
        news = new Properties();
        news.load(new FileInputStream(new File("db/news/"+news_name+".properties")));
        this.amount_nodes = amount_nodes;
        start();
    }
    
    public AgreementProtocolManager getProtocolManager(){
        return manager;
    }

    private void start() throws Exception{
        while (!manager.isDecided()) { // while not decided
            int vote = Integer.parseInt(news.getProperty("MEAN"));
            boolean fake = false; 
            if(vote <= 3){
                fake = true; // check whether the news is relevant (fake) or not
            }
            Vote v = new Vote(fake,manager.getRound(), manager.getWeight());
            Package pack = new Package(2,"my vote for the news "+news_name+ " is "+fake, v); // encapsulate it
            Message message = new Message(null, pack); 
            connectionHandler.send(message);// send
            // while quantidade de votos recebidos < numero de ativos - faltosos
            while(manager.getVote0()+manager.getVote1() < (amount_nodes - faulty())){
                System.out.println("Esperando mensagens");
            }
            System.out.println("OK");
            break;
        }
    }

    private int faulty() {
        return Math.abs(amount_nodes - connectionHandler.connected());
    }
}
