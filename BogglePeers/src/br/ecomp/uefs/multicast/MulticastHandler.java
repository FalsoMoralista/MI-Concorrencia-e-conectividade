/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.multicast;

import br.ecomp.uefs.game.Game;
import br.ecomp.uefs.game.InGameScreen;
import br.ecomp.uefs.game.Word;
import br.ecomp.uefs.model.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * This is be responsible for handling requests and parsing messages through the
 * network.
 *
 * @see MultiPackage
 * @see MultiRequest
 * @author Luciano Araujo Dourado Filho
 */
public class MulticastHandler implements Runnable, Serializable {

    private Peer peer;
    private byte[] data;

    MulticastHandler(Peer peer, byte[] data) {
        this.peer = peer;
        this.data = data;
    }

    @Override
    public void run() {
        ObjectInputStream is = null;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            is = new ObjectInputStream(in);
            handle(is.readObject());
        } catch (IOException ex) {
            Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handle(Object readObject) throws IOException, Exception {

        if (readObject instanceof MultiPackage) {
            MultiPackage pack = (MultiPackage) readObject;
            if (pack.getID().equals(peer.toString())) {
                return;
            }
            switch (pack.getOP()) {
                case "hello": // in case an user is identifying himself on the network
                    if (pack.hasAttachment()) {
                        peer.addToGroup((User) pack.getATTACHMENT());
                    }
                    break;
                case "all": // case an user is sending a linked list of users
                    if (pack.hasAttachment()) {
                        peer.getGroup().addCollection((LinkedList<User>) pack.getATTACHMENT());
                    }
                    break;
                case "s": // (starting game)
                    if (pack.hasAttachment()) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    new InGameScreen((Game) pack.getATTACHMENT(), (User) peer).start(new Stage());
                                } catch (IOException ex) {
                                    Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (Exception ex) {
                                    Logger.getLogger(MulticastHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                    }
                    break;
                case "w": // (word)
                    if (pack.hasAttachment()) {
                        peer.getGroup().addUserMessage(peer.toString(), pack.getID(), (Word) pack.getATTACHMENT());
                    }
                    break;
            }
        } else if (readObject instanceof MultiRequest) {
            MultiRequest pack = (MultiRequest) readObject;
            if (pack.getRequester().equals(peer.toString())) {
                return;
            }
            switch (pack.getREQ_OP()) {
                case "w": { // in case some is requesting a word from an user.

                    String usr = pack.getFrom(); // get the user which the word hasn't delivered.

                    CommunicationGroup group = peer.getGroup(); // get its group

                    LinkedList<User> participants = group.getParticipants(); // participants

                    User u = null;

                    Iterator it = participants.iterator();

                    while (it.hasNext()) {
                        User aux = (User) it.next();
                        if (aux.toString().equals(usr)) {
                            u = aux; // find him according to its id
                        }
                    }
                    LinkedList<Word> usrWords = u.getGroup().getMessages().get(usr); // get his wordlist
                    Word w = usrWords.get(pack.getWordNumber()); // get the missing word.

                    u.multicast(new MultiResponse(pack.getRequester(), usr, w)); // this is a response containing:
                    // the lost word from usr, to pack.getRequester()
                }
            }
        } else if (readObject instanceof MultiRequest) {
            MultiResponse pack = (MultiResponse) readObject;
            if (!pack.getTo().equals(peer.toString())) { // check if the message belongs to me
                return;
            }
             peer.getGroup().addUserMessage(peer.toString(), pack.getFrom(), pack.getResponse()); // otherwise, try to add to the user
        }

    }

}
