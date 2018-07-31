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
        channel.setReceiver(this);
        channel.connect("MainCluster");
    }

    /**
     *
     * @param new_view
     */
    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    /**
     * Receives a message
     *
     * @param msg
     */
    @Override
    public void receive(Message msg) {
        System.out.println(msg.getSrc() + ": " + msg.getObject());
        try {
            handle(msg);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Multicast a message to the connected group.
     *
     * @param msg
     * @throws java.lang.Exception
     */
    public void send(Message msg) throws Exception {
        channel.send(msg);
    }

    @Override
    public void handle(Message message) throws IOException, Exception{
        if (message.getObject() instanceof Package) {
            Package pack = (Package) message.getObject();
            switch (pack.getType()) {
                case 1: // IN CASE AGREEMENT
                    int id = Integer.parseInt(pack.getMessage());
                    startAgreement(id);
                    break;
                case 2:
                    Vote v = (Vote) pack.getAttachment();
                    AgreementProtocolManager manager =  handler.getProtocolManager();
                    break;                    
            }
        }
    }

    private void startAgreement(int newsID) throws IOException, Exception {
        System.out.println("Starting  Agreement");
        handler = new AgreementHandler(newsID, this);
    }
    
    public Server getServer(){
        return server;
    }

}