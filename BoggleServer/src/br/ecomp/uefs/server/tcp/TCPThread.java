/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server.tcp;

import br.ecomp.uefs.exception.EmptyGroupException;
import br.ecomp.uefs.server.Server;
import shared.exception.UserAlreadyRegisteredException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedList;
import java.util.StringTokenizer;
import shared.exception.InsufficientAmountOfPlayersException;
import shared.exception.InvalidPasswordException;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.UserAlreadyBindedException;
import shared.model.Lobby;
import shared.model.LobbyParameter;
import shared.model.Session;
import br.ecomp.uefs.model.User;
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
            if (request instanceof Package) {
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

    private Package getRequest(Package request) throws IOException {
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
                            return new Package("ERROR", "exception", ex);
                        }
                    }
                    case "login": // tries to authenticate an user with the server, if it does, binds the user to a session and returns it back to the client
                        try {
                            Session session = (Session) request.getCONTENT();
                            User user = (User) server.login(session.getUsername(), session.getPassword());
                            return new Package("OK", "session", new Session(user));
                        } catch (InvalidPasswordException ex) {
                            System.out.println(ex);
                            return new Package("ERROR", "exception", ex);
                        }
                    case "lobby":
                        LobbyParameter lobbyRequest = (LobbyParameter) request.getCONTENT();
                        try {
                            Lobby userLobby = server.bindUserToLobby(lobbyRequest.getLobbyNumber(), lobbyRequest.getUser());
                            return new Package("OK", "lobby", userLobby);
                        } catch (UserAlreadyBindedException | MaxAmountOfPlayersReachedException ex) {
                            return new Package("ERROR", "exception", ex);
                        } catch (EmptyGroupException ex) {
                            return new Package("ERROR", "exception", ex);
                        }
                    case "game":
                        int id = (int) request.getCONTENT();
                        try {
                            Object game = server.startGame(id);
                            return new Package("OK", "game", game);
                        } catch (InsufficientAmountOfPlayersException ex) {
                            return new Package("ERROR", "exception", ex);
                        } catch (UnknownHostException ex) {
                            Logger.getLogger(TCPThread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    default:
                        return new Package("ERROR", "invalid type of request", "null");
                }
            case "GET":
                switch (request.getTYPE()) {
                    case "lobbies":
                        LinkedList<Object> lobbies = server.listRooms();
                        return new Package("OK", "lobbies", lobbies);
                    default:
                        return new Package("ERROR", "invalid type of request", "null");
                }
            case "DEL":
                switch (request.getTYPE()) {
                    default:
                        return new Package("ERROR", "invalid type of request", "null");
                }
        }
        return new Package("ERROR", "invalid type of request", "null");
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
