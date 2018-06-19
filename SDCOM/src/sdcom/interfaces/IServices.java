/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import sdcom.model.Product;

/**
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public interface IServices extends Remote{
        
    public boolean sell(Product product) throws RemoteException;        

    public Product get(int ID) throws RemoteException;        
    
    public void add(Product product) throws RemoteException;
}
