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
public class ClientNotFoundException extends Exception {

    public ClientNotFoundException() {
        super("error -> client not found in records");
    }
    
}
