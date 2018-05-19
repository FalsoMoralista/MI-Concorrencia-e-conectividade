/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ecomp.uefs.exception;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class EmptyGroupException extends Exception {

    public EmptyGroupException() {
        super("Error -> Peer not binded to group, impossible to start communicating");
    }

}
