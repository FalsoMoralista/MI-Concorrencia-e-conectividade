/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import br.ecomp.uefs.model.User;
import java.io.Serializable;

/**
 *  Binds an user to a session.
 * @author Luciano Araujo Dourado Filho
 */
public class Session implements Serializable{

    private User user;
    
    private String username;
    private String password;
    
    public Session(String username, String password){
        this.username = username;
        this.password = password;
    }
    
    public Session(User user) {
        this.user = user;
    }
    
    /**
     *  Returns the user binded to this session. 
     */
    public User getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
