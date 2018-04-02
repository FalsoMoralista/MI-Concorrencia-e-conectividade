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
public class WaterFlowMeasurer implements IObserver{
    
    private double waterFlow;
    private boolean updated;

    @Override
    public void Update() {        
    }
        
}
