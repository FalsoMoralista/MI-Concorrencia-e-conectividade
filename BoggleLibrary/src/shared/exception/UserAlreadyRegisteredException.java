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
public class UserAlreadyRegisteredException extends Exception {
    
    public UserAlreadyRegisteredException() {
        super("Error -> User already registered");
    }    
}
