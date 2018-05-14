/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.exception;

/**
 *
 * @author luciano
 */
public class InvalidDataException extends Exception {

    public InvalidDataException() {
        super("error: Invalid data.");
    }
    
}
