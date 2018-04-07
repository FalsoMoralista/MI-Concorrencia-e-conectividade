/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

/**
 *
 * @author luciano
 */
public class Client extends ClientServer{

    private String address;
    private String zone;

    public Client(String address, String zone) {
        super();
        this.address = address;
        this.zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Client{" + "address=" + address + ", zone=" + zone + '}';
    }
    
}
