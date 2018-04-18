/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.interfaces;

import br.com.inova.of.things.exceptions.ClientMeasurerNotFoundException;
import br.com.inova.of.things.model.Observer;

/**
 *
 * @author luciano
 */
public interface ISubject {
    public void attach(Observer observer);
    public void detach(Observer observer);
    public Observer getObserver(String key) throws ClientMeasurerNotFoundException;
}
