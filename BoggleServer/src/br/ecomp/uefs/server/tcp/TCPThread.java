/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server.tcp;

import br.ecomp.uefs.server.Server;
import shared.exception.UserAlreadyRegisteredException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.StringTokenizer;
import shared.util.ClientServer;
import shared.util.Package;

/**
 * This is the request handler. It will get request Packages and parse them
 * dealing with the server.
 *
 * @author luciano
 */
public class TCPThread extends Thread {

    private Socket socket;
    private Server server;

    public TCPThread(Socket s, Server server) {
        this.socket = s;
        this.server = server;
    }

    @Override
    public void run() {
        ObjectOutputStream outToClient = null;
        ObjectInputStream inFromClient = null;
        try {
            inFromClient = new ObjectInputStream(socket.getInputStream());
            outToClient = new ObjectOutputStream(socket.getOutputStream());
            Object request = inFromClient.readObject(); // get the request
            System.out.println("Package received from -> " + socket.getInetAddress().toString() + " via TCP");
            Package response = null;
            if(request instanceof Package){
                response = getRequest((Package) request);
            }
            outToClient.writeObject(response);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {           
                inFromClient.close();
                outToClient.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Package getRequest(Package request) {
        switch (request.getHEADER()) {
            case "PUT": // IN CASE SOME TRY TO PUT
                switch (request.getTYPE()) {
                    case "user": // IF TRIES TO PUT AN USER:
                         {
                            try {
                                server.putUser(request.getCONTENT()); // TRY TO REGISTER THE USER ON THE SERVER
                                return new Package("OK", "client registered sucessfully", "null");
                            } catch (UserAlreadyRegisteredException ex) {
                                System.out.println(ex.getMessage());
                                return new Package("ERROR","null",ex);
                            }
                        }
                }
                break;
            case "GET":
                break;
            case "DEL":
                break;
        }
        return new Package("ERROR","invalid type of request","null");
    }

    private LinkedList<String> parse(String message) {
        LinkedList<String> parsed = new LinkedList<>();
        message = message.substring(message.indexOf("["), message.length());
        StringTokenizer token = new StringTokenizer(message, "|");
        while (token.hasMoreTokens()) {
            String str = token.nextToken();
            parsed.add(str);
        }
        return parsed;
    }
}
