/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.interfaces.ISubject;
import java.util.HashMap;

/**
 *
 * @author luciano
 */
public class Subject implements ISubject {

    private HashMap<String, Observer> observers;

    public Subject() {
        observers = new HashMap<>();
    }

    @Override
    public void attach(Observer observer) {
        observers.put(observer.toString(), observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer.toString(), observer);
    }
    
    public Observer getObserver(String key){
        return observers.get(key);
    }

}
