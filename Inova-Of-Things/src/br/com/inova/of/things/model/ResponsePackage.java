/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.Serializable;

/**
 *
 * @author luciano
 */
public class ResponsePackage implements Serializable{
    
    private String RESPONSE_TYPE;
    private String RESPONSE_CONTENT;
    private Object RESPONSE_OBJ;

    public ResponsePackage(String RESPONSE_TYPE, String RESPONSE_CONTENT) {
        this.RESPONSE_TYPE = RESPONSE_TYPE;
        this.RESPONSE_CONTENT = RESPONSE_CONTENT;
    }

    public ResponsePackage(String RESPONSE_TYPE, String RESPONSE_CONTENT, Object RESPONSE_OBJ) {
        this.RESPONSE_TYPE = RESPONSE_TYPE;
        this.RESPONSE_CONTENT = RESPONSE_CONTENT;
        this.RESPONSE_OBJ = RESPONSE_OBJ;
    }
    
    public boolean hasObj(){
        return this.RESPONSE_OBJ == null;
    }

    public Object getRESPONSE_OBJ() {
        return RESPONSE_OBJ;
    }

    public void setRESPONSE_OBJ(Object RESPONSE_OBJ) {
        this.RESPONSE_OBJ = RESPONSE_OBJ;
    }

    
    
    public String getRESPONSE_TYPE() {
        return RESPONSE_TYPE;
    }

    public void setRESPONSE_TYPE(String RESPONSE_TYPE) {
        this.RESPONSE_TYPE = RESPONSE_TYPE;
    }

    public String getRESPONSE_CONTENT() {
        return RESPONSE_CONTENT;
    }

    public void setRESPONSE_CONTENT(String RESPONSE_CONTENT) {
        this.RESPONSE_CONTENT = RESPONSE_CONTENT;
    }
    
    
}
