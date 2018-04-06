/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

/**
 *
 * @author luciano
 * @param <Time>
 * @param <Measure>
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
