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
        
    private double waterConsumed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public WaterFlowMeasurer(String id){
        super();
        this.id = id;
    }
    
    public double getWaterConsumed() {
        return waterConsumed;
    }

    public void setWaterConsumed(double waterConsume) {
        this.waterConsumed = waterConsume;
    }
    
    public void addConsume(double val){
        this.waterConsumed += val;
    }
    
    @Override
    public void Update() {
        System.out.println("updating ...");
    }
        
    @Override
    public String toString() {
        return id;
    }
    
    
}
