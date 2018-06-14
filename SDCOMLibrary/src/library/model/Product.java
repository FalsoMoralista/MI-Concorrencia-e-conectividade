/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package library.model;

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

    @Override
    public String toString() {
        return "{" + ID + '}';
    }        
}
