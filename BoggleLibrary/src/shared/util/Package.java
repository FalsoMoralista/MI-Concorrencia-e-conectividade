/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.io.Serializable;

/**
 *  This class can be used to encapsulate messages between server and other applications.
 *  
 * @author luciano
 */
public class Package implements Serializable{

    private String HEADER;
    private String TYPE;
    private Object CONTENT;

    public Package(String HEADER, String TYPE, Object CONTENT) {
        this.HEADER = HEADER;
        this.TYPE = TYPE;
        this.CONTENT = CONTENT;
    }

    public void setHEADER(String HEADER) {
        this.HEADER = HEADER;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public void setCONTENT(Object CONTENT) {
        this.CONTENT = CONTENT;
    }
   
    public String getHEADER() {
        return HEADER;
    }

    public String getTYPE() {
        return TYPE;
    }

    public Object getCONTENT() {
        return CONTENT;
    }
            
}
