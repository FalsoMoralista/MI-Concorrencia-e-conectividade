/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class CommunicationGroup implements Serializable{

    private InetAddress groupAddress;
    
    public CommunicationGroup(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
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
