/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.model;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Ranking implements Comparable<Ranking>{
    
    private String user;
    private int score;

    public Ranking(String user, int score) {
        this.user = user;
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Ranking o) {
        return score - o.getScore();
    }

    @Override
    public String toString() {
        return user + ", score= " + score;
    }
        
    
}
