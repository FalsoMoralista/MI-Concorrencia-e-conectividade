/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.multicast;

import java.io.Serializable;

/**
 * This will be used to send request through the network.
 * @author Luciano Araujo Dourado Filho
 */
public class MultiRequest implements Serializable{
    
    private String REQ_ID;
    private String REQ_OP;
    private int number;

    public MultiRequest(String REQ_ID, String REQ_OP, int number) {
        this.REQ_ID = REQ_ID;
        this.REQ_OP = REQ_OP;
        this.number = number;
    }

    public String getREQ_ID() {
        return REQ_ID;
    }

    public void setREQ_ID(String REQ_ID) {
        this.REQ_ID = REQ_ID;
    }

    public String getREQ_OP() {
        return REQ_OP;
    }

    public void setREQ_OP(String REQ_OP) {
        this.REQ_OP = REQ_OP;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    
}
