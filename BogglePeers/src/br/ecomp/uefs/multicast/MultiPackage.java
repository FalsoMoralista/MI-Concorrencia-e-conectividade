/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.multicast;

import java.io.Serializable;

/**
 *  This will be used to handle the communication between peers.
 * @author Luciano Araujo Dourado Filho
 */
public class MultiPackage implements Serializable{
    
    private String ID;
    private String OP;
    private Object ATTACHMENT;

    public MultiPackage(String ID, String OP) {
        this.ID = ID;
        this.OP = OP;
    }

    public MultiPackage(String ID, String OP, Object ATTACHMENT) {
        this.ID = ID;
        this.OP = OP;
        this.ATTACHMENT = ATTACHMENT;
    }
            
    public boolean hasAttachment(){
        return this.ATTACHMENT != null;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOP() {
        return OP;
    }

    public void setOP(String OP) {
        this.OP = OP;
    }

    public Object getATTACHMENT() {
        return ATTACHMENT;
    }

    public void setATTACHMENT(Object ATTACHMENT) {
        this.ATTACHMENT = ATTACHMENT;
    }
    
    
    
    public static void main(String[] args){
        MultiPackage p = new MultiPackage("0", "1","n");
        System.out.println(p.hasAttachment());
    }
}
