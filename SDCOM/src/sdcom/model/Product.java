/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sdcom.model;

import java.io.Serializable;

/**
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public class Product implements Serializable{
    
    private String name;
    private final int ID;
    private int quantity;
    private double price;

    public Product(String name, int ID, int quantity,double price) {
        this.name = name;
        this.ID = ID;
        this.quantity = quantity;
        this.price = price;
    }
                
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
             
    @Override
    public String toString() {
        return "[" + "ID=" + ID + ", name=" + name + "]";
    }           
}
