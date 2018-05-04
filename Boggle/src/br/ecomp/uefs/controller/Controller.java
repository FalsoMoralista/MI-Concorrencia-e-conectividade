/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.controller;

import shared.model.User;
import java.io.IOException;
import java.net.UnknownHostException;
import shared.exception.InvalidPasswordException;
import shared.exception.UserAlreadyRegisteredException;
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
     */
    public boolean authenticate(String login, String password) throws IOException, UnknownHostException, ClassNotFoundException, InvalidPasswordException {
        Session session = new Session(login, password);
        Package request = new Package(PUT, "login", session);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "exception":
                        InvalidPasswordException ex = (InvalidPasswordException) pack.getCONTENT();
                        throw ex;
                }
            case "OK":
                switch(pack.getTYPE()){
                    case"session":
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
     */
    public void register(User newUser) throws ClassNotFoundException, IOException, UserAlreadyRegisteredException {
        Package request = new Package(PUT, "user", newUser);
        Package pack = (Package) ClientServer.sendTCP(serverHost, 8888, request);
        switch (pack.getHEADER()) {
            case "ERROR":
                switch (pack.getTYPE()) {
                    case "exception":
                        UserAlreadyRegisteredException ex = (UserAlreadyRegisteredException) pack.getCONTENT();
                        throw ex;
                }
        }
    }

    private void parse(Package response) {
        
    }

    public static void main(String[] args) throws ClassNotFoundException, IOException, UserAlreadyRegisteredException, UnknownHostException, InvalidPasswordException {
        Controller c = new Controller("localhost");
        User luciano = new User("luciano", "123");
//        c.register(luciano);
        System.out.println(c.authenticate("luciano","123"));
        System.out.println(c.getInstance());
    }
}
