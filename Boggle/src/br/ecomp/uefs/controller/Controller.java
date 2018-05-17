/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.controller;

import shared.model.User;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import shared.exception.InsufficientAmountOfPlayersException;
import shared.exception.InvalidPasswordException;
import shared.exception.InvalidTypeOfRequestException;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.NoneLogInException;
import shared.exception.UserAlreadyBindedException;
import shared.exception.UserAlreadyRegisteredException;
import shared.model.Game;
import shared.model.Lobby;
import shared.model.LobbyParameter;
import shared.model.Session;
import shared.util.ClientServer;
import shared.util.Package;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Controller {

    private User instance;

    private String serverHost;
    private final int TCP = 8888;

    private final String PUT = "PUT";
    private final String GET = "GET";
    private final String DEL = "DEL";

    public Controller(String serverHost) {
        this.serverHost = serverHost;
    }

    /**
     * Returns the current user using this.
     *
     * @return
     * @see User
     *
     */
    public User getInstance() {
        return instance;
    }
    
    /**
     * Try to authenticate an user at the server.
     *
     * @param login
     * @param password
     * @return
     * @throws java.io.IOException
     * @throws java.net.UnknownHostException
     * @throws java.lang.ClassNotFoundException
     * @throws shared.exception.InvalidPasswordException
     * @throws shared.exception.InvalidTypeOfRequestException
     */
    public boolean authenticate(String login, String password) throws IOException, UnknownHostException, ClassNotFoundException, InvalidPasswordException, InvalidTypeOfRequestException {
        Session session = new Session(login, password);
        Package request = new Package(PUT, "login", session);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "exception":
                        InvalidPasswordException ex = (InvalidPasswordException) pack.getCONTENT();
                        throw ex;
                    default:
                        throw new InvalidTypeOfRequestException();
                }
            case "OK":
                switch (pack.getTYPE()) {
                    case "session":
                        Session s = (Session) pack.getCONTENT();
                        this.instance = s.getUser();
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * Register a new user on the server.
     *
     * @param newUser
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     * @throws shared.exception.UserAlreadyRegisteredException
     * @throws shared.exception.InvalidTypeOfRequestException
     */
    public void register(User newUser) throws ClassNotFoundException, IOException, UserAlreadyRegisteredException, InvalidTypeOfRequestException {
        Package request = new Package(PUT, "user", newUser);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "exception":
                        UserAlreadyRegisteredException ex = (UserAlreadyRegisteredException) pack.getCONTENT();
                        throw ex;
                    default:
                        throw new InvalidTypeOfRequestException();
                }
        }
    }

    /**
     * Return a list of all available rooms in the server.
     *
     * @return @throws IOException
     * @throws UnknownHostException
     * @throws ClassNotFoundException
     * @throws shared.exception.InvalidTypeOfRequestException
     */
    public LinkedList<Lobby> getAvailableRooms() throws IOException, UnknownHostException, ClassNotFoundException, InvalidTypeOfRequestException {
        Package request = new Package("GET", "lobbies", null);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "OK":
                switch (pack.getTYPE()) {
                    case "lobbies":
                        LinkedList<Lobby> lobbies = (LinkedList<Lobby>) pack.getCONTENT();
                        return lobbies;
                }
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "invalid type of request":
                        throw new InvalidTypeOfRequestException();
                }
        }
        return null;
    }

    /**
     *
     * @param lobbyNumber
     * @return
     * @throws shared.exception.InvalidTypeOfRequestException
     * @throws java.io.IOException
     * @throws java.net.UnknownHostException
     * @throws java.lang.ClassNotFoundException
     * @throws shared.exception.UserAlreadyBindedException
     * @throws shared.exception.MaxAmountOfPlayersReachedException
     */
    public Lobby enterLobby(int lobbyNumber) throws InvalidTypeOfRequestException, IOException, UnknownHostException, ClassNotFoundException, UserAlreadyBindedException, MaxAmountOfPlayersReachedException, NoneLogInException{
        Lobby userLobby = null;
        if (instance instanceof User) {
            LobbyParameter lobbyRequest = new LobbyParameter(instance, lobbyNumber);
            Package request = new Package("PUT", "lobby", lobbyRequest);
            Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
            switch (pack.getHEADER()) {
                case "OK":
                    switch (pack.getTYPE()) {
                        case "lobby":
                            userLobby = (Lobby) pack.getCONTENT();
                            return userLobby;
                    }
                case "ERROR":
                    switch (pack.getTYPE()) {
                        case "exception":
                            if(pack.getCONTENT() instanceof UserAlreadyBindedException ){
                                throw (UserAlreadyBindedException)pack.getCONTENT();
                            }else{
                                throw (MaxAmountOfPlayersReachedException)pack.getCONTENT();
                            }
                        case "invalid type of request":
                            throw new InvalidTypeOfRequestException();
                    }
            }
        } else {
            throw new NoneLogInException();
        }
        return userLobby;
    }
    
    public Game startGame(int id) throws IOException, UnknownHostException, ClassNotFoundException, InsufficientAmountOfPlayersException{
        Package request = new Package("PUT", "game", id);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "OK":
                switch (pack.getTYPE()) {
                    case "game":
                        Game game  = (Game) pack.getCONTENT();
                        return game;
                }
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "exception":
                        throw new InsufficientAmountOfPlayersException();
                }
        }
        return null;
        
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, UserAlreadyRegisteredException, UnknownHostException, InvalidPasswordException, InvalidTypeOfRequestException, Exception {
        Controller c = new Controller("localhost");
        User luciano = new User("luciano", "123");
        c.register(luciano);
        System.out.println(c.authenticate("luciano", "123"));
        System.out.println(c.getInstance());
        LinkedList<Lobby> list = c.getAvailableRooms();
        System.out.println("Available rooms:");
        list.forEach(System.out::println);
        System.out.println("Waiting on lobby:");
        System.out.println(c.enterLobby(0).getAmountOfPlayers());
    }
}
