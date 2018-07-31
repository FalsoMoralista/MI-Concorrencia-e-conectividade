/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.inova.services;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class AgreementProtocolManager {
    
    private boolean decided;
    private boolean vote; // my vote
    private int round;
    private int weight; // my vote's weight 
    private int vote0; // amount of 0 votes
    private int vote1; // amount of 1 votes
    private int witness0; // amount of witness that voted 0
    private int witness1; // amount of witness that voted 1

    public AgreementProtocolManager() {
        weight = 1;
        round = 1;
        decided = false;
    }    
    
    public boolean isVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
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

    public int getVote0() {
        return vote0;
    }

    public void setVote0(int vote0) {
        this.vote0 = vote0;
    }

    public int getVote1() {
        return vote1;
    }

    public void setVote1(int vote1) {
        this.vote1 = vote1;
    }

    public int getWitness0() {
        return witness0;
    }

    public void setWitness0(int witness0) {
        this.witness0 = witness0;
    }

    public int getWitness1() {
        return witness1;
    }

    public void setWitness1(int witness1) {
        this.witness1 = witness1;
    }

    public boolean isDecided() {
        return decided;
    }

    public void setDecided(boolean decided) {
        this.decided = decided;
    }            
}
