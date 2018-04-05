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

    String address;
    String zip;
    String region;

    public Client(String address, String zip, String region) {
        this.address = address;
        this.zip = zip;
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Client{" + "address=" + address + ", zip=" + zip + ", region=" + region + '}';
    }
    
}
