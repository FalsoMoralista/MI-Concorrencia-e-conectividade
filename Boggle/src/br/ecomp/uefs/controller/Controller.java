/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.controller;

import br.ecomp.uefs.model.User;

/**
 *
 * @author luciano
 */
public class Controller {
    
    private User instance;
    
    private String host;
    private final int TCP = 8888;
    private final int UDP = 1111;
    
    private final String PUT = "PUT";
    private final String GET = "GET";
    private final String DEL = "DEL";
    

    public Controller(String host) {
        this.host = host;
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
     */
    public void register(User newUser){        
    }
    
    /**
     *  Remove an user from the server given its name. 
     * @param username
     */
    public void remove(String username){        
    }
        
}
