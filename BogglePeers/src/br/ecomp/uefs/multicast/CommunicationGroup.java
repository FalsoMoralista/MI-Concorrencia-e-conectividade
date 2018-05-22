/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.multicast;

import br.ecomp.uefs.game.Word;
import br.ecomp.uefs.model.User;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
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

    public void addUserMessage(String userID, Word message) throws IOException {
        System.out.println("Adicionando a palavra numero: " + message.getNumber() + " (" + message.getWord() + ") " + " ao usuario: " + userID);

        LinkedList<Word> words = groupMessages.get(userID);

        System.out.println("Participantes:");
        participants.forEach(System.out::println);

        System.out.println("Palavras atuais do usuario " + userID);
        words.forEach(System.out::println);

        if (!words.contains(message)) {
            if (words.size() > 0) {
                try {
                    System.out.println(words.get(message.getNumber() - 1).getWord());
                } catch (NullPointerException ex) {

                    Iterator i = participants.iterator();

                    User u = null;

                    while(i.hasNext()){
                        User aux = (User) i.next();
                        if(aux.toString().equals(userID)){
                            u = aux;
                        }
                    }
                    
                    u.multicast(new MultiRequest(userID,"w",message.getNumber()-1));
                }
            } else {
                if (message.getNumber() == 0) {
                    words.add(message);
                }else{
                    // pedir mensagem nao recebida na rede.
                }
            }
        }
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
