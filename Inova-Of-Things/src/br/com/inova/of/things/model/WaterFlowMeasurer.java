/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.interfaces.IObserver;

/**
 *
 * @author luciano
 */
public class WaterFlowMeasurer extends Observer{
    
    private String id;
    private String associated;

    private double waterFlow;
    private boolean updated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public WaterFlowMeasurer(String id){
        this.id = id;
    }
    
    private String readFlow(){
        return "";
    }


    public String getAssociated() {
        return associated;
    }

    public void setAssociated(String associated) {
        this.associated = associated;
    }
    
}
