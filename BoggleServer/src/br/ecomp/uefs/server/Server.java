/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server;

import br.ecomp.uefs.multicast.CommunicationGroup;
import br.ecomp.uefs.exception.EmptyGroupException;
import shared.exception.UserAlreadyRegisteredException;
import shared.exception.UserNotFoundException;
import br.ecomp.uefs.server.tcp.TCPListener;
import br.ecomp.uefs.server.tcp.TCPThread;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import shared.exception.InsufficientAmountOfPlayersException;
import shared.exception.InvalidPasswordException;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.UserAlreadyBindedException;
import br.ecomp.uefs.game.Game;
import shared.model.Lobby;
import br.ecomp.uefs.model.User;

/**
 * This class will be the runner for all services implemented.
 *
 * @see TCPListener
 * @see TCPThread
 * @author Luciano Araujo Dourado Filho
 */
public class Server extends Thread {

    private HashMap<String, Object> users;
    private HashMap<Integer,Object> rooms;

    private LinkedList<Object> lobbies;
    private static final int MAX_NUMBER_OF_ROOMS = 10;
    
    public Server() throws UnknownHostException {
        users = new HashMap<>();
        lobbies = new LinkedList<>();
        String addr = "230.0.0.0";
        for(int i = 0; i < MAX_NUMBER_OF_ROOMS; i++){
            lobbies.add(new Lobby(i,new CommunicationGroup(InetAddress.getByName(addr))));
            addr = "230.0.0."+Integer.toString(i);
        }
        new Thread(this).start();
    }

    /*-------------------------  USER CONTROL --------------------------------*/
    /**
     * Register an user to the server.
     *
     * @param user
     * @throws shared.exception.UserAlreadyRegisteredException
     */
    public void putUser(Object user) throws UserAlreadyRegisteredException {
        System.out.println("Server.trying to register user");
        try {
            users.get(user.toString()).toString();
            throw new UserAlreadyRegisteredException();
        } catch (NullPointerException ex) {
            users.put(user.toString(), user);
            System.out.println("Server-> User registered sucessfully");
        }
    }
    /*------------------------------------------------------------------------*/
    /**
     * Delete an user from the server.
     *
     * @param user
     * @throws shared.exception.UserNotFoundException
     */
    public void deleteUser(Object user) throws UserNotFoundException {
        System.out.println("Server.trying to remove user");
        try {
            this.users.get(user.toString()).toString();
            this.users.remove(user.toString(), user);
            System.out.println("Server -> User removed sucessfully");
        } catch (NullPointerException ex) {
            throw new UserNotFoundException();
        }
    }
    /*------------------------------------------------------------------------*/
    /**
     * Returns an user given it's key
     *
     * @param key
     * @return
     */
    public Object getUser(String key) {
        return this.users.get(key);
    } 
    /*------------------------------------------------------------------------*/    
    /**
     *  Authenticates an user given it's user name and password.
     * @param username
     * @param password
     * @return 
     * @throws shared.exception.InvalidPasswordException
     */
    public Object login(String username, String password) throws InvalidPasswordException {
        System.out.println("Server.login attempt");
        User u = (User)users.get(username);
        if(u.getPassword().equals(password)){
            System.out.println("Server -> sucessfully authenticated");
            return u;
        }
        throw new InvalidPasswordException();
    }
    /*-------------------------- GAME CONTROL --------------------------------*/
    /**
     * Associates an user to a lobby given its number. 
     * @param roomNumber
     * @param user
     * @return 
     * @throws shared.exception.UserAlreadyBindedException
     * @throws shared.exception.MaxAmountOfPlayersReachedException
     * @throws java.io.IOException
     * @throws br.ecomp.uefs.exception.EmptyGroupException
     */
    public Lobby bindUserToLobby(int roomNumber,Object user) throws UserAlreadyBindedException, MaxAmountOfPlayersReachedException, IOException, EmptyGroupException{
        System.out.println("Server.binding user to lobby");
        Lobby lobby = (Lobby) lobbies.get(roomNumber);
        User usr = (User) user;
        lobby.bindUser(usr);
        System.out.println("Server -> user binded");
        return lobby;
    }
    /*------------------------------------------------------------------------*/
    /**
     * Removes an user from a lobby. 
     * @param roomNumber
     * @param user
     */
    public void removeUserFromLobby(int roomNumber,Object user){
        System.out.println("Server.removing user from lobby");
        Lobby lobby = (Lobby) lobbies.get(roomNumber);
        User usr = (User) user;
        lobby.removeUser(usr);        
        System.out.println("Server -> user removed");
    }
    /*------------------------------------------------------------------------*/
    /**
     *  Return the actual number of players online for a given room. 
     * @param roomNumber
     * @return 
     */
    public int getRoomAmountOfPlayers(int roomNumber){
        System.out.println("Server.retrieving room number of players");
        Lobby lobby = (Lobby) lobbies.get(roomNumber);
        System.out.println("Server -> retrieved");
        return lobby.getAmountOfPlayers();        
    }
    /*------------------------------------------------------------------------*/
    /**
     *  Returns a list of all lobbies available.
     * @return 
     */
    public LinkedList<Object> listRooms(){
        System.out.println("Server -> retrieving available lobbies");
        return lobbies;
    } 
    /*------------------------------------------------------------------------*/
    /**
     *  Starts the game. 
     * @param lobbyID
     * @throws shared.exception.InsufficientAmountOfPlayersException
     * @throws java.net.UnknownHostException
     */
    public Object startGame(int lobbyID) throws InsufficientAmountOfPlayersException, UnknownHostException{
        Game game = null;
        Lobby lobby = (Lobby) lobbies.get(lobbyID);
        if(lobby.getAmountOfPlayers() < 2){
            throw new InsufficientAmountOfPlayersException();
        }else{
            HashMap<String,User> players = lobby.getPlayers();
            game = new Game(players, null);
            lobbies.remove(lobbyID);
//            CommunicationGroup group = new CommunicationGroup(InetAddress.getByName("230.0.0"+Integer.toString(lobbies.size()+1)));
//            lobbies.add(new Lobby(lobbies.size()+1,group));
        }
        return game;
    }    
    /*------------------------------------------------------------------------*/

    @Override
    public void run() {
        System.out.println("[ Server started ]");
        TCPListener tcp = new TCPListener(this);
    }

}
