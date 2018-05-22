/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.multicast;

import br.ecomp.uefs.model.Ranking;
import br.ecomp.uefs.game.Word;
import br.ecomp.uefs.model.User;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class CommunicationGroup implements Serializable {

    private InetAddress groupAddress;

    private LinkedList<User> participants;

    private HashMap<String, LinkedList<Word>> groupMessages;

    public CommunicationGroup(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
        this.participants = new LinkedList<>();
        this.groupMessages = new HashMap<>();
    }

    public InetAddress getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
    }

    @Override
    public String toString() {
        return groupAddress.toString();
    }

    public LinkedList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(LinkedList<User> participants) {
        this.participants = participants;
    }

    public void addCollection(LinkedList<User> collec) {
        collec.forEach(c -> {
            if (!participants.contains(c)) {
                participants.add(c);
            }
        });
        System.out.println("total de players final, apos start: " + participants.size());
    }

    /**
     * Adds an participant to the group.
     *
     * @param obj
     */
    public void addParticipant(User obj) {
        if (!participants.contains(obj)) {
            participants.add(obj);
            groupMessages.put(obj.toString(), new LinkedList<>());
        }
    }

    public void addUserMessage(String me, String userID, Word message) throws IOException {
        System.out.println("Adicionando a palavra numero: " + message.getNumber() + " (" + message.getWord() + ") " + " ao usuario: " + userID);

        LinkedList<Word> words = groupMessages.get(userID); // get the wordlist from the user

        System.out.println("Participantes:");
        participants.forEach(System.out::println);

        if (!words.contains(message)) { // if the doesn't contains the word
            if (words.size() > 0) {
                try {
                    words.get(message.getNumber() - 1).getWord(); // try to get the previous word if(there are more than 0 words.
                    words.add(message);
                } catch (NullPointerException ex) { // if there is more than 0 words on the list and the previous word is not on the list do:

                    Iterator i = participants.iterator();

                    User u = null;

                    while (i.hasNext()) { // find myself to send a request through the network
                        User aux = (User) i.next();
                        if (aux.toString().equals(me)) {
                            u = aux;
                        }
                    }
                    u.multicast(new MultiRequest(me, userID, "w", message.getNumber() - 1)); // me requesting the word number (message.getNumber()-1) from userID                    
                }
            } else {
                if (message.getNumber() == 0) {
                    words.add(message);
                } else {
                    try {
                        words.get(message.getNumber() - 1).getWord(); // try to get the previous word if(there are more than 0 words.
                        words.add(message);
                    } catch (NullPointerException ex) { // if there is more than 0 words on the list and the previous word is not on the list do:

                        Iterator i = participants.iterator();

                        User u = null;

                        while (i.hasNext()) { // find myself to send a request through the network
                            User aux = (User) i.next();
                            if (aux.toString().equals(me)) {
                                u = aux;
                            }
                        }
                        u.multicast(new MultiRequest(me, userID, "w", 0)); // me requesting the word number (0) from userID                    
                    }
                }
            }
        }
        System.out.println("Palavras atuais do usuario " + userID);
        words.forEach(System.out::println);
    }
    
    /**
     *  Accounts the game score.
     *  For each word, check if any other player have one equal, if not, account.
     */
    public LinkedList<Ranking> account(){
        LinkedList<Ranking> ranking = new LinkedList<>();
        participants.forEach(player -> {
            Ranking r = null;
            
            LinkedList<Word> wordlist = groupMessages.get(participants.toString()); // get the user 1 wordlist

            int[] score = new int[1];   
            
            wordlist.forEach(word ->{
                boolean[]op = new boolean[1];
                op[0] = true;
                for(int i = 0; i < participants.size(); i++){
                    User u = participants.get(i);
                    LinkedList<Word> userWords = groupMessages.get(u.toString());
                    userWords.forEach(userWord->{
                        if(word.getWord().equals(userWord.getWord())){
                            op[0] = false;
                        }
                    });
                }
                if(op[0]){
                    score[0] += word.getWord().length();
                }
            });
            r = new Ranking(player.toString(), score[0]);
            ranking.add(r);
        });
        Collections.sort(ranking);
        return ranking;
    }

    public HashMap<String, LinkedList<Word>> getMessages() {
        return groupMessages;
    }

    public static void main(String[] args) throws IOException {

        LinkedList<User> users = new LinkedList<>();

        User a = new User("a", "a");
        User b = new User("b", "b");
        User c = new User("c", "c");

        CommunicationGroup g = new CommunicationGroup(null);

        g.addParticipant(a);

        users.add(a);
        users.add(b);
        users.add(c);

        System.out.println("tam antes " + g.participants.size());

        g.addCollection(users);

        System.out.println("tam despues " + g.participants.size());

    }
}
