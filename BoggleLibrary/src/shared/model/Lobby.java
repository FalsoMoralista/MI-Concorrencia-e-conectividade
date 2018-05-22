/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import br.ecomp.uefs.model.User;
import br.ecomp.uefs.CommunicationGroup;
import br.ecomp.uefs.exception.EmptyGroupException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.UserAlreadyBindedException;

/**
 * This will associate users to a room before they start a game.
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Lobby implements Serializable {

    private HashMap<String, User> players;

    private User leader;

    private int id;
    
    private CommunicationGroup group;
    
    private static final int MAX_NUMBER_OF_PLAYERS = 10;

    public Lobby(int id, CommunicationGroup group) {
        players = new HashMap<>();
        this.group = group;
        this.id = id;
    }

    /**
     * Returns this lobby id.
     *
     * @return
     */
    public int getId() {
        return id;
    }

    public CommunicationGroup getGroup(){
        return this.group;
    }
    
    /**
     * Returns the current user that leads this lobby.
     *
     * @return
     */
    public User getLeader() {
        return leader;
    }

    /**
     * Binds the an user to the room.
     *
     * @param u
     * @throws shared.exception.UserAlreadyBindedException
     * @throws shared.exception.MaxAmountOfPlayersReachedException
     * @throws java.io.IOException
     * @throws br.ecomp.uefs.exception.EmptyGroupException
     */
    public void bindUser(User u) throws UserAlreadyBindedException, MaxAmountOfPlayersReachedException, IOException, EmptyGroupException {
        if (players.size() < MAX_NUMBER_OF_PLAYERS) {
            if (players.size() == 0) {
                leader = u;
            }
            if (!players.containsKey(u.toString())) {
                players.put(u.toString(), u);
            } else {
                throw new UserAlreadyBindedException();
            }
        } else {
            throw new MaxAmountOfPlayersReachedException();
        }
    }

    /**
     * Remove the user from this lobby.
     *
     * @param u
     */
    public void removeUser(User u) {
        players.remove(u.toString(), u);
    }

    /**
     * Returns an user given its key.
     *
     * @param key
     * @return
     */
    public User getPlayer(String key) {
        return players.get(key);
    }

    /**
     * Returns the players binded to this lobby.
     *
     * @return
     */
    public HashMap<String, User> getPlayers() {
        return players;
    }

    /**
     * Return the amount of players in this room.
     *
     * @return
     */
    public int getAmountOfPlayers() {
        return players.size();
    }

    @Override
    public String toString() {
        return "Lobby-" + id + "[" + "players: " + this.getAmountOfPlayers() + "/" + MAX_NUMBER_OF_PLAYERS + ']';
    }

    public static void main(String[] args) throws UserAlreadyBindedException, MaxAmountOfPlayersReachedException, IOException, EmptyGroupException {
        Lobby lobby = new Lobby(0,new CommunicationGroup(InetAddress.getByName("230.0.0.255")));
        
        User u = new User("luciano", "123");
        lobby.bindUser(u);        


        User usr = new User("USERNAME", "PASS");


        lobby.bindUser(usr);
        
        u.start();
        usr.start();
        
        u.multicast("testando");

//        System.out.println(lobby.getAmountOfPlayers() == 1);
//        System.out.println(lobby.getPlayer("luciano"));
//        lobby.removeUser(u);
//        lobby.bindUser(u);
//        lobby.bindUser(u);
    }
}
