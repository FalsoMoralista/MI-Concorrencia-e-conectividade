/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.game;

import java.io.Serializable;
import java.util.HashMap;
import br.ecomp.uefs.multicast.CommunicationGroup;
import br.ecomp.uefs.model.GameDices;
import br.ecomp.uefs.model.User;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This represents the Game.
 * @author Luciano Araujo Dourado Filho
 */
public class Game implements Serializable {

    protected HashMap<String, User> users;

    protected CommunicationGroup group;

    protected long STARTING_TIME;
            
    protected LinkedList<String> letterSet = new LinkedList<>();
       
    protected GameDices dices;

    
    public Game(HashMap<String, User> users, CommunicationGroup group) {
        this.users = users;
        this.group = group;
        this.dices = new GameDices();
        this.shuffleDices();
        this.randomizeDices();
        this.start();
    }

    public LinkedList<String> getLetterSet() {
        return letterSet;
    }

    public void setLetterSet(LinkedList<String> letterSet) {
        this.letterSet = letterSet;
    }

    public GameDices getDices() {
        return dices;
    }

    public void setDices(GameDices dices) {
        this.dices = dices;
    }

    public long getSTARTING_TIME() {
        return STARTING_TIME;
    }           

    public HashMap<String, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public CommunicationGroup getGroup() {
        return group;
    }

    public void setGroup(CommunicationGroup group) {
        this.group = group;
    }

    /**
     * Shuffles all the 16 dices.
     *
     * @see GameDices
     */
    public void shuffleDices() {
        List<String> row = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++) {
                row.add(dices.DICES[i][j]); // get all letters from a dice and
            }                               // adds it to a list
            Collections.shuffle(row);       // then shuffles it.
            int j = 0;
            for (String letter : row) {
                dices.DICES[i][j++] = letter;
            }
            row.clear();
        }
    }

    /**
     * Generate the random letters for the dices and add them to a set.
     */
    public void randomizeDices() {

        int minimum = 0;
        int maximum = 5;

        Random rn = new Random();

        int n = maximum - minimum + 1;

        String[] randomized = new String[16];
        for (int row = 0; row < 16; row++) {
            int randomNum = rn.nextInt() % n;
            int column = Math.abs(minimum + randomNum);
            randomized[row] = dices.DICES[row][column];
        }

        int get = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                letterSet.add(randomized[get++]);
            }
        }
    }

    /**
     * Reset all configurations.
     */
    public void reset() {
        this.shuffleDices();
        this.randomizeDices();
        this.letterSet.clear();
    }
    
    public void start(){
        STARTING_TIME = System.currentTimeMillis();        
    }
    
    public static void main(String[] args){
        Game g = new Game(null, null);        
    }
}
