/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.io.Serializable;
import java.util.HashMap;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.UserAlreadyBindedException;

/**
 * This will associate users to a room before they start a game.
 * @author Luciano Araujo Dourado Filho
 */
public class Lobby implements Serializable{

    private HashMap<String, User> players;
    private static final int MAX_NUMBER_OF_PLAYERS = 10;
    
    public Lobby(){
        players = new HashMap<>();
    }
    
    /**
     *  Binds the an user to the room. 
     * @param u
     * @throws shared.exception.UserAlreadyBindedException
     * @throws shared.exception.MaxAmountOfPlayersReachedException
     */
    public void bindUser(User u) throws UserAlreadyBindedException, MaxAmountOfPlayersReachedException {
        if (players.size() < MAX_NUMBER_OF_PLAYERS) {
            if (!players.containsKey(u.toString())) {
                players.put(u.toString(), u);
            }else{
                throw new UserAlreadyBindedException();
            }
        }else{
            throw new MaxAmountOfPlayersReachedException();
        }
    }
    
    /**
     *  Remove the user from this lobby. 
     * @param u
     */
    public void removeUser(User u){
        players.remove(u.toString(),u);
    }

    
    /**
     *  Returns an user given its key. 
     * @param key
     * @return 
     */
    public User getPlayer(String key){
        return players.get(key);
    }
    
    /**
     *  Returns the players binded to this lobby. 
     * @return 
     */
    public HashMap<String,User> getPlayers(){
        return players;
    }
    
    /**
     *  Return the amount of players in this room. 
     * @return 
     */
    public int getAmountOfPlayers(){
        return players.size();
    }        
        
    public static void main(String[] args) throws UserAlreadyBindedException, MaxAmountOfPlayersReachedException{
        Lobby lobby = new Lobby();
        User u = new User("luciano","123");
        lobby.bindUser(u);
        System.out.println(lobby.getAmountOfPlayers() == 1);
        System.out.println(lobby.getPlayer("luciano"));
        lobby.removeUser(u);
        lobby.bindUser(u);
        lobby.bindUser(u);        
    }
}
