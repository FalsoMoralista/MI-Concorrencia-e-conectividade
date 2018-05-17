/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.io.Serializable;
import java.util.HashMap;
import shared.util.CommunicationGroup;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Game implements Serializable{

    protected HashMap<String, User> users;
    protected CommunicationGroup group;

    public Game(HashMap<String, User> users, CommunicationGroup group) {
        this.users = users;
        this.group = group;
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public CommunicationGroup getGroup() {
        return group;
    }

    public void setGroup(CommunicationGroup group) {
        this.group = group;
    }
    
    
}
