/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.io.Serializable;

/**
 * This class is used to encapsulate a message containing the user and the room
 * number from the room in which he's trying to enter.
 *
 * @author Luciano Araujo Dourado Filho
 */
public class LobbyParameter implements Serializable {

    private User user;
    private int lobbyNumber;

    public LobbyParameter(User user, int lobbyNumber) {
        this.user = user;
        this.lobbyNumber = lobbyNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getLobbyNumber() {
        return lobbyNumber;
    }

    public void setLobbyNumber(int lobbyNumber) {
        this.lobbyNumber = lobbyNumber;
    }

}
