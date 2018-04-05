/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.exceptions;

/**
 *
 * @author luciano
 */
public class ClientAlreadyRegisteredException extends Exception {

    public ClientAlreadyRegisteredException() {
        super("error -> client already registered");
    }
    
}
