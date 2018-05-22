/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.multicast;

import br.ecomp.uefs.game.Word;
import java.io.Serializable;

/**
 *  This will be used to send responses over the network.
 *  to : for the user that is requesting.
 *  from: the user that word hasn't delivered.
 *  
 * @author Luciano Araujo Dourado Filho
 */
public class MultiResponse implements Serializable{
    
    private String to;
    private String from;
    private Word response;

    public MultiResponse(String to, String from, Word response) {
        this.to = to;
        this.from = from;
        this.response = response;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Word getResponse() {
        return response;
    }

    public void setResponse(Word response) {
        this.response = response;
    }
    
    
}
