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
public class RequestPackage implements Serializable{

    private String METHOD;
    private String OBJECT_TYPE;
    private String CONTENT;

    public RequestPackage(String METHOD, String OBJECT_TYPE, String CONTENT) {
        this.METHOD = METHOD;
        this.OBJECT_TYPE = OBJECT_TYPE;
        this.CONTENT = CONTENT;
    }

    public String getMETHOD() {
        return METHOD;
    }

    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public String getOBJECT_TYPE() {
        return OBJECT_TYPE;
    }

    public void setOBJECT_TYPE(String OBJECT_TYPE) {
        this.OBJECT_TYPE = OBJECT_TYPE;
    }

    public String getCONTENT() {
        return CONTENT;
    }

    public void setCONTENT(String CONTENT) {
        this.CONTENT = CONTENT;
    }
    
    
}
