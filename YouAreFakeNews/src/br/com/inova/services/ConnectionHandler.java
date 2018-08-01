/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.services;

import interfaces.Handler;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import br.com.inova.model.Package;
import br.com.inova.model.Vote;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class ConnectionHandler extends ReceiverAdapter implements Handler {

    private JChannel channel;
    private Server server;

    private AgreementHandler handler;

    private final String AGREEMENT = "1";

    public ConnectionHandler(Server server) throws Exception {
        this.server = server;
        connect();
    }

    private void connect() throws Exception {
        channel = new JChannel();
        channel.setName(server.getSERVICE_NAME());
        channel.setReceiver(this);
        channel.connect("MainCluster");
    }

    /**
     * Return how many nodes are connected to the cluster.
     *
     * @return
     */
    public int connected() {
        return channel.getView().getMembers().size();
    }

    /**
     * Show connections status.
     *
     * @param new_view
     */
    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
        System.out.println("There are " + connected() + " nodes connected");
    }

    /**
     * Receives a message
     *
     * @param msg
     */
    @Override
    public void receive(Message msg) {
        Runnable r = () -> {
            System.out.println(msg.getSrc() + ": " + msg.getObject());
            try {
                handle(msg);
            } catch (Exception ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        new Thread(r).start();
    }

    /**
     * Multicast a message to the connected group. Waits for a random space of
     * time before sending a message through the network so that if there's a
     * delayed message coming through the network, its probability of arriving
     * at time will be increased.
     *
     * @param msg
     * @throws java.lang.Exception
     */
    public void send(Message msg) throws Exception {
        Random random = new Random();
        Thread.sleep((random.nextInt(10) + 1) * 200);
        channel.send(msg);
    }

    @Override
    public void handle(Message message) throws IOException, Exception {

        if (message.getObject() instanceof Package) {
            Package pack = (Package) message.getObject();
            switch (pack.getType()) {
                case 1: // IN CASE STARTING AGREEMENT
                    int id = Integer.parseInt(pack.getMessage());
                    startAgreement(id);
                    break;
                case 2: // IN CASE OCCURING AN AGREEMENT
                    Vote v = (Vote) pack.getAttachment();
                    if (handler == null) { // check if the previous message had arrive. if not, wait for an random space of time so the possibility of arriving increases. 
                        Random random = new Random();
                        Thread.sleep((random.nextInt(10) + 1) * 100);
                    }

                    AgreementProtocolManager manager = handler.getProtocolManager();

                    if (v.getRound() > manager.getRound()) { // checks if the message is delayed (process it later)
                        Runnable r = () -> {
                            Package pkg = new Package(2, "delayed message", v);
                            Message msg = new Message(null, pkg);
                            try {
                                send(msg); // send it again untill the previous message arrive
                            } catch (Exception ex) {
                                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        };
                        new Thread(r).start();
                    } else if (v.getRound() == manager.getRound()) { // if get in the correct round 
                        boolean myVote = manager.getVote();
                        if (v.isFake()) {
                            if (myVote) {
                                manager.setWeight(manager.getWeight() + 1);
                            }
                            manager.setVote1(manager.getVote1() + 1);// increment the amount of 1(fake) votes
                        } else {
                            if (!myVote) {
                                manager.setWeight(manager.getWeight() + 1);
                            }
                            manager.setVote0(manager.getVote0() + 1);// increment the amount of 0(non-fake) votes
                        }
                        if (v.getWeight() > (connected() / 2)) { // checks whether the weight of this vote is major then half of the connected nodes
                            if (v.isFake()) {
                                manager.setWitness1(manager.getWitness1() + 1);// increment the amount of 1(fake) witness
                            } else {
                                manager.setWitness0(manager.getWitness0() + 1);// increment the amount of 0(fake) witness
                            }
                        }
                        manager.setRound(manager.getRound() + 1); // increment the amount of rounds
                    }
                    System.out.println("Quantidade de votos 0: " + manager.getVote0());
                    System.out.println("Quantidade de votos 1: " + manager.getVote1());
                    System.out.println("---------------------------------------------------------------- ");
                    System.out.println();
                    System.out.println("Quantidade de Testemunhas 0: " + manager.getWitness0());
                    System.out.println("Quantidade de Testemunhas 1: " + manager.getWitness1());
                    
                    break;
            }
        }
    }

    private void startAgreement(int newsID) throws IOException, Exception {
        System.out.println("Starting  Agreement");
        handler = new AgreementHandler(newsID, this, connected());
    }

    public Server getServer() {
        return server;
    }

}
