/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.inova.exception;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class NoAverageException extends Exception {

    public NoAverageException() {
        super("Error, no values available to compute mean");
    }

}
