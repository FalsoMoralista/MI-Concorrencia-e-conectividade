/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server;

import shared.exception.UserAlreadyRegisteredException;
import shared.exception.UserNotFoundException;
import br.ecomp.uefs.server.tcp.TCPListener;
import br.ecomp.uefs.server.tcp.TCPThread;
import java.util.HashMap;

/**
 * This class will be the runner for all services implemented.
 *
 * @see TCPListener
 * @see TCPThread
 * @author luciano
 */
public class Server extends Thread {

    private HashMap<String, Object> users;
    

    public Server() {
        this.users = new HashMap<>();
        new Thread(this).start();
    }
    
    /**
     * Register an user to the server. 
     * @param obj the user
     * @throws shared.exception.UserAlreadyRegisteredException
     */
    public void putUser(Object obj) throws UserAlreadyRegisteredException{
        System.out.println("Server.trying to register user");        
        try{
            users.get(obj.toString()).toString();
            throw new UserAlreadyRegisteredException();
        }catch(NullPointerException ex){
            users.put(obj.toString(), obj);
            System.out.println("Server-> User registered sucessfully");
        }
    }
    
    /**
     * Delete an user from the server. 
     * @param obj
     * @throws shared.exception.UserNotFoundException
     */
    public void deleteUser(Object obj) throws UserNotFoundException{
        System.out.println("Server.trying to remove user");
        try{
            this.users.get(obj.toString()).toString();
            this.users.remove(obj.toString(),obj);
            System.out.println("Server -> User removed sucessfully");
        }catch(NullPointerException ex){
            throw new UserNotFoundException();
        }
    }

    @Override
    public void run() {
        System.out.println("[ Server started ]");
        TCPListener tcp = new TCPListener(this);
    }
        
}
