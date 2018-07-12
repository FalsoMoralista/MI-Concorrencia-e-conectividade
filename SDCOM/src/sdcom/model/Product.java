/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.model;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a product.
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public class Product implements Serializable {

    private String name;
    private final int ID;
    private int quantity;
    private double price;
    private boolean locked;

    public Product(String name, int ID, int quantity, double price) {
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

    /**
     *  Lock the product so that two users don't buy the same. 
     */
    public synchronized void lock() {
        System.out.println("Locking the product for a minute");
        locked = true;
        Runnable r = () -> {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Product.class.getName()).log(Level.SEVERE, null, ex);
            }
            unlock();
        };
        new Thread(r).start();
    }
    
    /**
     *  Return whether the product is locked or not.
     * @return 
     */
    public boolean isLocked(){
        return locked;
    }
    
    private void unlock() {
        System.out.println("Unlocking");
        locked = false;
    }

    @Override
    public String toString() {
        return ID + "|" + name + "|" + price + "|" + quantity + "|";
    }
}
