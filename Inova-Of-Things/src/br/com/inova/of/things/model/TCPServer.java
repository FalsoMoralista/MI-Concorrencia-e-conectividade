/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.exceptions.ClientMeasurerNotFoundException;
import br.com.inova.of.things.exceptions.ClientNotFoundException;
import br.com.inova.of.things.exceptions.InvalidTypeOfRequestException;
import br.com.inova.of.things.interfaces.IMyServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class TCPServer extends Subject implements Runnable, IMyServer {

    private int port;

    private Server server;
    private Socket socket;
    private ServerSocket serverSocket;
    private final String RESPONSE_TYPE_OK = "OK";
    private final String RESPONSE_TYPE_ERROR = "ERROR";

    private HashMap<String, Client> clients = new HashMap<>();

    private boolean listening;

    public TCPServer(int port, Server server) throws IOException {
        this.server = server;
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.startListen();
        new Thread(this).start();
    }

    /**
     * Start to listen.
     */
    @Override
    public void startListen() {
        this.listening = true;
    }

    /**
     * Stop listening.
     */
    @Override
    public void stopListening() {
        this.listening = false;
    }

    @Override
    public void run() {
        System.out.println("Server.started listening to the port -> " + port + " via TCP...");
        while (listening) {
            try {
                socket = serverSocket.accept();
                ObjectOutputStream outToClient = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream inFromClient = new ObjectInputStream(socket.getInputStream());
                Object request = inFromClient.readObject(); // get the request
                System.out.println("Package received from -> " + socket.getInetAddress().toString() + " via TCP");
                ResponsePackage response = null;
                if (request instanceof RequestPackage) {
                    response = getRequest((RequestPackage) request); // parse the request
                }
                outToClient.writeObject(response); // send back a response to the client
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private LinkedList<String> parse(String message) {
        LinkedList<String> parsed = new LinkedList<>();
        StringTokenizer token = new StringTokenizer(message, "|");
        while (token.hasMoreTokens()) {
            String str = token.nextToken();
            parsed.add(str);
        }
        return parsed;
    }

    /**
     * Parse a request object sent from a client.
     *
     * @param pack the package
     */
    private ResponsePackage getRequest(RequestPackage pack) {
        switch (pack.getMETHOD()) { // GETS THE MESSAGE METHOD
            case "POST": // POST METHOD :
                switch (pack.getOBJECT_TYPE()) {  // GETS THE OBJECT TYPE
                    case "client": // IF POST-CLIENT:
                        LinkedList<String> info = this.parse(pack.getCONTENT()); // PARSE THE PACKAGE CONTENT
                        return this.registrate(info); // TRY TO REGISTER A NEW CLIENT
                    default: // CASE THE MESSSAGE DON'T FOLLOW THE PROTOCOL:
                        return new ResponsePackage(RESPONSE_TYPE_ERROR, new InvalidTypeOfRequestException().getMessage()); //  THROW BACK AN ERROR MESSAGE
                }
            case "DELETE": // DELETE METHOD : 
                switch (pack.getOBJECT_TYPE()) {
                    case "client": // IF DELETE-CLIENT:
                        LinkedList<String> info = this.parse(pack.getCONTENT());
                        return this.delete(info);
                    default:
                        return new ResponsePackage(RESPONSE_TYPE_ERROR, new InvalidTypeOfRequestException().getMessage());
                }
            case "GET": // GET METHOD:
                switch (pack.getOBJECT_TYPE()) {
                    case "client": // IF GET-CLIENT:
                        LinkedList<String> info = this.parse(pack.getCONTENT()); // PARSE THE PACKAGE CONTENT
                        return this.getClient(info); // TRY TO GET THE CLIENT FROM SERVER
                    case "measurer": // IF GET-MEASURER":
                        LinkedList<String> clInfo = this.parse(pack.getCONTENT()); // PARSE THE PACKAGE CONTENT
                        return this.getObserver(clInfo); // TRY TO GET THE MEASURER FROM SERVER                        
                    default: // CASE THE MESSAGE DON'T FOLLOW THE PROTOCOL
                        return new ResponsePackage(RESPONSE_TYPE_ERROR, new InvalidTypeOfRequestException().getMessage()); // THROW BACK ERROR MESSAGE
                }
            default: // CASE DON'T FOLLOW THE PROTOCOL :
                return new ResponsePackage(RESPONSE_TYPE_ERROR, new InvalidTypeOfRequestException().getMessage()); // THROW BACK ERROR MESSAGE
        }
    }

    private ResponsePackage getObserver(LinkedList<String> clInfo) {
        try {
            Observer obs = this.server.getObserver(clInfo.get(0));
            obs.toString();
            return new ResponsePackage(RESPONSE_TYPE_OK, obs.toString(), obs);
        } catch (ClientMeasurerNotFoundException ex) {
            return new ResponsePackage(RESPONSE_TYPE_ERROR, ex.getMessage());
        }
    }

    private ResponsePackage getClient(LinkedList<String> clientInfo) {
        try {
            Client c = this.server.getClient(clientInfo.get(0));
            return new ResponsePackage(RESPONSE_TYPE_OK, c.toString(), c);
        } catch (ClientNotFoundException ex) {
            return new ResponsePackage(RESPONSE_TYPE_ERROR, ex.getMessage());
        }
    }

    private ResponsePackage delete(LinkedList<String> clientInfo) {
        try {
            this.server.remove(clientInfo.get(0));
            return new ResponsePackage(RESPONSE_TYPE_OK, "Server.message -> client removed sucessfully");
        } catch (ClientAlreadyRemovedException ex) {
            return new ResponsePackage(RESPONSE_TYPE_ERROR, ex.getMessage());
        }

    }

    private ResponsePackage registrate(LinkedList<String> clientInfo) {
        String email = clientInfo.get(0);
        String address = clientInfo.get(1);
        String zone = clientInfo.get(2);
        try {
            this.server.registrate(new Client(email, address, zone));
            return new ResponsePackage(RESPONSE_TYPE_OK, "Server.message -> client registered sucessfully");
        } catch (ClientAlreadyRegisteredException ex) {
            return new ResponsePackage(RESPONSE_TYPE_ERROR, ex.getMessage());
        }
    }
}
