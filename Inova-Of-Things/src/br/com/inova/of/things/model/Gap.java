/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

/**
 * This class is used to encapsulate a gap of time and a valued measured in the
 * given gap.
 * @author luciano
 * @param <T>
 * @param <M>
 */
public class Gap <T,M>{
    private T time;
    private M measure;

    public Gap(T time, M measure) {
        this.time = time;
        this.measure = measure;
    }

    public T getTime() {
        return time;
    }

    public void setTime(T time) {
        this.time = time;
    }

    public M getMeasure() {
        return measure;
    }

    public void setMeasure(M measure) {
        this.measure = measure;
    }

    @Override
    public String toString() {
        return "Gap{" + "time=" + time + ", measure=" + measure + '}';
    }

    
}
