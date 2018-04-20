/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author luciano
 */
public class ClientMeasure implements Serializable{
    private String id;
    private LocalDateTime time;
    private double reading;

    public ClientMeasure(String id, LocalDateTime time, double reading) {
        this.id = id;
        this.time = time;
        this.reading = reading;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }

    @Override
    public String toString() {
        return "ClientMeasure{" + "id=" + id + ", time=" + time + ", reading=" + reading + '}';
    }
    
}
