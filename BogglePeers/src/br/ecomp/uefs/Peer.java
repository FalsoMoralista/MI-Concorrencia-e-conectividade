/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs;

import br.ecomp.uefs.exception.EmptyGroupException;
import br.ecomp.uefs.model.User;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Peer implements Serializable{

    private CommunicationGroup group;
    private transient MulticastListener listener;
    private transient MulticastPublisher publisher;

    public Peer() throws SocketException, IOException {
    }

    public CommunicationGroup getGroup() {
        return group;
    }

    public void setGroup(CommunicationGroup group) {
        this.group = group;
    }
    
    /**
     *  Add a participant to the group. 
     * @param participant
     */
    public void addToGroup(User participant){
        this.group.addParticipant(participant);
    }
    
    /**
     *  Multicast a message through the group. 
     * @param message
     * @throws java.io.IOException
     */
    public void multicast(Object message) throws IOException{
        publisher.multicast(message);
    }

    /**
     * Start.
     * @throws java.io.IOException
     * @throws br.ecomp.uefs.exception.EmptyGroupException
     */
    public void start() throws IOException, EmptyGroupException {
        if (group == null) {
            throw new EmptyGroupException();
        } else {
            this.listener = new MulticastListener(this);
            this.publisher = new MulticastPublisher(this);
        }
    }

}
