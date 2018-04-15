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
public class ClientMeasurerNotFoundException extends Exception {

    public ClientMeasurerNotFoundException() {
        super("error -> client measurer not found in records");
    }
    
}
