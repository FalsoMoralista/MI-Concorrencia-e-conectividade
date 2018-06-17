/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sdcom.model;

/**
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public class Product {
    
    private final long ID;
    private String name;

    public Product(long ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getID() {
        return ID;
    }
           
    @Override
    public String toString() {
        return "{" + ID + '}';
    }        
}
