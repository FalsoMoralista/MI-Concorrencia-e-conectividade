/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class ServiceLoader {

    public static void main(String[] args) throws FileNotFoundException, IOException, RemoteException, AlreadyBoundException, NotBoundException {
        Properties services = new Properties();
        services.load(new FileInputStream(new File("resources/services.properties")));
        Server[] servers = new Server[services.size()];
        for (int i = 0; i < services.size()-1; i++) {
            String SERVICE_NAME = services.getProperty("SERVICE_NAME" + '[' + Integer.toString(i) + ']');
            servers[i] = new Server(SERVICE_NAME);
            servers[i].run();
        }                
    }
}
