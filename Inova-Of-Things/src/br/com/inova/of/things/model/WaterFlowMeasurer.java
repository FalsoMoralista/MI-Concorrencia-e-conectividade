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
        
    private double waterConsume;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public WaterFlowMeasurer(String id){
        this.id = id;
    }
    
    public double getWaterConsume() {
        return waterConsume;
    }

    public void setWaterConsume(double waterConsume) {
        this.waterConsume = waterConsume;
    }
    

    @Override
    public void Update() {
        System.out.println("updating ...");
    }
        
    @Override
    public String toString() {
        return "WaterFlowMeasurer{" + "id=" + id + '}';
    }
    
    
}
