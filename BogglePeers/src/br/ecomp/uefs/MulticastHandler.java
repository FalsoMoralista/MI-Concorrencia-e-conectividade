/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class MulticastHandler implements Runnable , Serializable{

    private Peer peer;
    private byte[] data;

    MulticastHandler(Peer peer, byte[] data) {
        this.peer = peer;
        this.data = data;
    }

    @Override
    public void run() {
        ObjectInputStream is = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            is = new ObjectInputStream(in);
            handle(is.readObject());
        } catch (IOException ex) {
            Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handle(Object readObject) {
        String s = (String) readObject;
        System.out.println(s);
    }

}
