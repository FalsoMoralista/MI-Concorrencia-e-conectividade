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
        vote0 = 0;
        vote1 = 0;
        witness0 = 0;
        witness1 = 0;        
    }    
    
    public synchronized boolean getVote() {
        return vote;
    }

    public synchronized void setVote(boolean vote) {
        this.vote = vote;
    }

    public synchronized int getRound() {
        return round;
    }

    public synchronized void setRound(int round) {
        this.round = round;
    }

    public synchronized int getWeight() {
        return weight;
    }

    public synchronized void setWeight(int weight) {
        this.weight = weight;
    }

    public synchronized int getVote0() {
        return vote0;
    }

    public synchronized void setVote0(int vote0) {
        this.vote0 = vote0;
    }

    public synchronized int getVote1() {
        return vote1;
    }

    public synchronized void setVote1(int vote1) {
        this.vote1 = vote1;
    }

    public synchronized int getWitness0() {
        return witness0;
    }

    public synchronized void setWitness0(int witness0) {
        this.witness0 = witness0;
    }

    public synchronized int getWitness1() {
        return witness1;
    }

    public synchronized void setWitness1(int witness1) {
        this.witness1 = witness1;
    }

    public synchronized boolean isDecided() {
        return decided;
    }

    public synchronized void setDecided(boolean decided) {
        this.decided = decided;
    }            
}
