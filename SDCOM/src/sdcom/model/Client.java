/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sdcom.model;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import sdcom.interfaces.IServices;
import sdcom.service.Server;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Client implements Serializable{
    public static void main(String[] args) throws RemoteException, NotBoundException {        
        Registry reg = LocateRegistry.getRegistry("10.0.0.6", 1099);
        System.out.println(reg.list());
        IServices s =  (IServices) reg.lookup("amazon");
        s.add(new Product(0123523, "Agua mineral"));
    }
}
