/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.model;

import java.io.Serializable;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Vote implements Serializable{

    private boolean fake;
    private int round;
    private int weight;

    public Vote(boolean fake, int round, int weight) {
        this.fake = fake;
        this.round = round;
        this.weight = weight;
    }

    public boolean isFake() {
        return fake;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    
}
