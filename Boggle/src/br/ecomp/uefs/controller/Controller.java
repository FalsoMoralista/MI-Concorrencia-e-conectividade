/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.controller;

import br.ecomp.uefs.model.User;
import br.ecomp.uefs.util.ClientServer;
import br.ecomp.uefs.util.Package;
import java.io.IOException;
/**
 *
 * @author luciano
 */
public class Controller {
    
    private User instance;
    
    private String serverHost;
    private final int TCP = 8888;
    private final int UDP = 1111;
    
    private final String PUT = "PUT";
    private final String GET = "GET";
    private final String DEL = "DEL";
    

    public Controller(String serverHost) {
        this.serverHost = serverHost;
    }
    
    /**
     *  Returns the current user using this.
     * @return 
     * @see User
     * 
     */
    public User getInstance(){
        return instance;
    }
    
    /**
     *  Try to authenticate an user at the server. 
     * @param login
     * @param password
     * @return 
     */
    public boolean authenticate(String login, String password){
        return false;
    }
    
    /**
     *  Register a new user on the server. 
     * @param newUser
     * @throws java.lang.ClassNotFoundException
     * @throws java.io.IOException
     */
    public void register(User newUser) throws ClassNotFoundException, IOException{        
        Package request = new Package(PUT, "user", newUser);
        ClientServer.requestTCP(serverHost, 8888, request);
    }
    
    /**
     *  Remove an user from the server given its name. 
     * @param username
     */
    public void remove(String username){        
    }
        
    public static void main(String[] args) throws ClassNotFoundException, IOException{
        Controller c = new Controller("localhost");
        c.register(new User());
    }
}
