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
import java.io.FileOutputStream;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        news.load(new FileInputStream(new File("db/news/" + news_name + ".properties")));
        this.amount_nodes = amount_nodes;
        Runnable r = () -> {
            try {
                start();
            } catch (Exception ex) {
                Logger.getLogger(AgreementHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        new Thread(r).start();
    }

    public synchronized AgreementProtocolManager getProtocolManager() {
        return manager;
    }

    private void start() throws Exception {
        int rate = Integer.parseInt(news.getProperty("MEAN"));
        boolean fake = false;
        if (rate <= 3) {
            fake = true; // check whether the news is relevant (fake) or not
        }
        manager.setVote(fake);
        while (!manager.isDecided()) { // while not decided
            Vote v = new Vote(manager.getVote(), manager.getRound(), manager.getWeight());
            Package pack = new Package(2, "my vote for the news " + news_name + " is " + fake, v); // encapsulate it
            Message message = new Message(null, pack);
            connectionHandler.send(message);// send
            while (manager.getVote0() + manager.getVote1() < (amount_nodes - faulty())) {
                System.out.println("Waiting messages");
                Random random = new Random();
                Thread.sleep(10000); 
            }
            System.out.println("Done waiting");
            if (manager.getWitness1() > 0) { // if the amount of positive witnesses is bigger than 0 change my vote
                manager.setVote(true);
            } else { // if the amount of negative witnesses is bigger than 0 change my vote
                manager.setVote(false);
            }
            if (manager.getVote1() > manager.getVote0()) { // if the amount of positive votes is bigger than the amount of negative votes, make my vote positive then.
                manager.setVote(true);
            } else {
                manager.setVote(false);
            }
            if (manager.getVote()) { // if my vote became positive
                manager.setWeight(manager.getVote1()); // make the next vote's weight equals to the amount of positive votes
            } else {
                manager.setWeight(manager.getVote0());
            }
            if (manager.getWitness1() > faulty() || manager.getWitness0() > faulty()) {
                manager.setDecided(true);
                if (manager.getVote()) {
                    System.out.println("----> I have decided, " + news_name + ", YOU ARE FAKE NEWS! " + " <----");
                    news.setProperty("DECIDED", "TRUE");
                } else {
                    System.out.println("---->" + " I have decided, " + news_name + " is NOT FAKE! <----");
                    news.setProperty("DECIDED", "FALSE");
                }
                news.store(new FileOutputStream(new File("db/news/" + news_name + ".properties")), "");
            }
            manager.setRound(manager.getRound() + 1);
        }
        Vote vote = new Vote(manager.getVote(), manager.getRound(), (connectionHandler.connected() - faulty()));
        Package pack = new Package(2, "Sending my help to the other nodes", vote);
        Message message = new Message(null, pack);
        connectionHandler.send(message);
        // ##################################
        vote = new Vote(manager.getVote(), manager.getRound() + 1, (connectionHandler.connected() - faulty()));
        pack = new Package(2, "Sending my help to the other nodes", vote);
        message = new Message(null, pack);
        connectionHandler.send(message);
    }

    /**
     * Return the amount of faulty nodes in the communication group.
     *
     * @return
     */
    public int faulty() {
        return Math.abs(amount_nodes - connectionHandler.connected());
    }
}
