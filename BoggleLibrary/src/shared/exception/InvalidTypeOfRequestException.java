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
public class InvalidTypeOfRequestException extends Exception {

    public InvalidTypeOfRequestException() {
        super("Error -> Invalid type of request");
    }
}
