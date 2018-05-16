/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs;

import java.net.InetAddress;
import java.util.HashMap;
import shared.model.User;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class CommunicationGroup {

    private InetAddress groupAddress;
    protected HashMap<String,User> users;
    
    public CommunicationGroup(InetAddress groupAddress, HashMap<String,User> users) {
        this.groupAddress = groupAddress;
        this.users = users;
    }

    public InetAddress getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
    }

    @Override
    public String toString() {
        return  groupAddress.toString();
    }
       
    
}
