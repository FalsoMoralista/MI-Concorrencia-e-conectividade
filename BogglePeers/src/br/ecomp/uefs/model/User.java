/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.model;

import br.ecomp.uefs.multicast.CommunicationGroup;
import br.ecomp.uefs.multicast.Peer;
import br.ecomp.uefs.exception.EmptyGroupException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class User extends Peer implements Serializable {

    private String username;
    private String password;
               
    public User(String username, String password) throws IOException {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }        

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public void start() throws IOException, EmptyGroupException {
        super.start(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setGroup(CommunicationGroup group) {
        super.setGroup(group); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CommunicationGroup getGroup() {
        return super.getGroup(); //To change body of generated methods, choose Tools | Templates.
    }
       
    @Override
    public String toString() {
        return username;
    }        
    
    public static void main(String[] args) throws IOException, EmptyGroupException{
        
        User u = new User("test","123");
        u.setGroup(new CommunicationGroup(InetAddress.getByName("230.0.0.0")));

        User usr = new User("testing","123");
        usr.setGroup(new CommunicationGroup(InetAddress.getByName("230.0.0.0")));
        
        usr.start();        
        u.start();
        usr.multicast("testando");
    }
}
