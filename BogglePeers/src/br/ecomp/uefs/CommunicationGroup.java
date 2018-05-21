/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.LinkedList;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class CommunicationGroup implements Serializable {

    private InetAddress groupAddress;
    private LinkedList<Object> participants;

    public CommunicationGroup(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
        this.participants = new LinkedList<>();
    }

    public InetAddress getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
    }

    @Override
    public String toString() {
        return groupAddress.toString();
    }

    public LinkedList<Object> getParticipants() {
        return participants;
    }

    public void setParticipants(LinkedList<Object> participants) {
        this.participants = participants;
    }

    public void addCollection(LinkedList<Object> collec){
        collec.forEach( c ->{
            if(!participants.contains(c)){
                participants.add(c);
            }                
        });
    }
    
    
    /**
     * Adds an participant to the group.
     *
     * @param obj
     */
    public void addParticipant(Object obj) {
        System.out.println("adding participant");        
        if (!participants.contains(obj)) {
            participants.add(obj);
        }
        System.out.println("amount of participants:" +this.participants.size());
    }
}
