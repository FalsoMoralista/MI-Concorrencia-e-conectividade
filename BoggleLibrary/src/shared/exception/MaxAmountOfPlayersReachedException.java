/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.exception;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class MaxAmountOfPlayersReachedException extends Exception {

    public MaxAmountOfPlayersReachedException() {
        super("Error -> Max amount of players reached for this room");
    }

}
