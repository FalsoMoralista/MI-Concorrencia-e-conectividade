/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.inova.model;

import java.io.Serializable;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Package implements Serializable{

    private int type;
    private String message;
    private Object attachment;    

    public Package(int type, String message, Object attachment) {
        this.type = type;
        this.message = message;
        this.attachment = attachment;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return this.message; //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

