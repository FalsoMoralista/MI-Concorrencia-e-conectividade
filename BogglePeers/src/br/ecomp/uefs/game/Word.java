/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.game;

import java.io.Serializable;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Word implements Serializable{
    
    private String word;
    private int number;

    public Word(String word, int number) {
        this.word = word;
        this.number = number;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    
}
