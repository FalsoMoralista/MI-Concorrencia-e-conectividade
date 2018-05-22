/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.multicast;

import java.io.Serializable;

/**
 * This will be used to send requests through the network.
 * @author Luciano Araujo Dourado Filho
 */
public class MultiRequest implements Serializable{
    
    private String requester;
    private String from;
    private String REQ_OP;
    private int wordNumber;

    public MultiRequest(String requester, String from, String REQ_OP, int wordNumber) {
        this.requester = requester;
        this.from = from;
        this.REQ_OP = REQ_OP;
        this.wordNumber = wordNumber;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getREQ_OP() {
        return REQ_OP;
    }

    public void setREQ_OP(String REQ_OP) {
        this.REQ_OP = REQ_OP;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }
    
    
}
