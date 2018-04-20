/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.util.LinkedList;

/**
 *
 * @author luciano
 */
public class ClientRecord {
    private String id;
    private double total;
    private LinkedList<ClientMeasure> record;

    public ClientRecord(String id, double total, LinkedList<ClientMeasure> record) {
        this.id = id;
        this.total = total;
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LinkedList<ClientMeasure> getRecord() {
        return record;
    }

    public void setRecord(LinkedList<ClientMeasure> record) {
        this.record = record;
    }
    
    
}
