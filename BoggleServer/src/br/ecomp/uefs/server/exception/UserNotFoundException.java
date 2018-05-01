/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server.exception;

/**
 *
 * @author luciano
 */
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super("Error -> User not found");
    }
    
}
