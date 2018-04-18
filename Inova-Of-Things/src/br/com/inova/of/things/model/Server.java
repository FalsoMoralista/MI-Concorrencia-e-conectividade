/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.exceptions.ClientMeasurerNotFoundException;
import br.com.inova.of.things.exceptions.ClientNotFoundException;
import br.com.inova.of.things.interfaces.IObserver;
import br.com.inova.of.things.interfaces.ISubject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class Server implements ISubject, Runnable, IObserver {

    private static final int PORT = 8888;
    private Thread thread;
    private HashMap<String, Client> clients = new HashMap<>();
    private HashMap<String, WaterFlowMeasurer> devices = new HashMap<>();

    public Server() {
        this.clients = new HashMap<>();
        this.devices = new HashMap<>();
        new File("src/br/com/inova/of/things/server").mkdir();
        new File("src/br/com/inova/of/things/server/bin").mkdir();
        try {
            this.load();
        } catch (FileNotFoundException | ClassNotFoundException ex) {
            System.out.println(ex.getCause() + " | " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getCause() + " | " + ex.getMessage());
        }
        thread = new Thread(this);
        thread.start();
    }

    /*###################### CLIENTS #############################*/
    public Client getClient(String key) throws ClientNotFoundException {
        System.out.println("Server.retrieving -> client...");
        try {
            Client c = this.clients.get(key);
            c.toString();
            System.out.println("Done");
            return c;
        } catch (NullPointerException ex) {
            System.out.println(new ClientNotFoundException().getMessage());
            throw new ClientNotFoundException();
        }
    }

    public void registrate(Client c) throws ClientAlreadyRegisteredException {
        System.out.println("Server.registrating -> " + c.toString() + "...");
        try {
            Client retrieve = this.clients.get(c.getEmail());
            retrieve.toString();
            System.out.println(new ClientAlreadyRegisteredException().getMessage());
            throw new ClientAlreadyRegisteredException();
        } catch (NullPointerException ex) {
            this.clients.put(c.getEmail(), c);
            this.attach(new WaterFlowMeasurer(c.toString()));
            System.out.println("Done");
            try {
                this.save();
            } catch (IOException exc) {
                System.out.println(exc.getCause() + " | " + exc.getMessage());
            }
        }
    }

    public void remove(String key) throws ClientAlreadyRemovedException {
        System.out.println("Server.removing -> client...");
        try {
            Client retrieve = this.clients.get(key);
            this.clients.remove(key, this.clients.get(key));
            try {
                this.detach(this.getObserver(retrieve.toString()));
            } catch (ClientMeasurerNotFoundException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                this.save();
            } catch (IOException ex) {
                System.out.println(ex.getCause() + " | " + ex.getMessage());
            }
            System.out.println("Done");
        } catch (NullPointerException ex) {
            throw new ClientAlreadyRemovedException();
        }
    }

    /* ####################### OBSERVER ############################# */
    @Override
    public void Update() {
        System.out.println("Server.updating...");
    }

    @Override
    public Observer getObserver(String key) throws ClientMeasurerNotFoundException{
        System.out.println("Server.retrieving -> observer...");
        try {
            Observer obs = this.devices.get(key);
            obs.toString();
            return obs;
        } catch (NullPointerException ex) {
            throw new ClientMeasurerNotFoundException();
        }
    }

    @Override
    public void attach(Observer observer) {
        System.out.println("Server.attaching -> " + observer.toString() + "...");
        this.devices.put(observer.toString(), (WaterFlowMeasurer) observer);
        System.out.println("Done");
    }

    @Override
    public void detach(Observer observer) {
        System.out.println("Server.detaching-> " + observer.toString() + "...");
        this.devices.remove(observer.toString(), observer);
        System.out.println("Done");
    }

    /*######################### SERVER #####################################*/
    private void load() throws FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println("Server.loading...");
        // tcp (clients)
        FileInputStream instream = new FileInputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectInputStream ois = new ObjectInputStream(instream);
        this.clients = (HashMap<String, Client>) ois.readObject();
        // udp (devices)
        instream = new FileInputStream(new File("src/br/com/inova/of/things/server/bin/udp.bin"));
        ois = new ObjectInputStream(instream);
        this.devices = (HashMap<String, WaterFlowMeasurer>) ois.readObject();
        System.out.println("Done");
    }

    private void save() throws FileNotFoundException, IOException {
        System.out.println("Server.saving...");
        // tcp (clients)
        FileOutputStream stream = new FileOutputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(this.clients);
        // udp (devices)
        stream = new FileOutputStream(new File("src/br/com/inova/of/things/server/bin/udp.bin"));
        oos = new ObjectOutputStream(stream);
        oos.writeObject(this.devices);
        System.out.println("Done");
    }

    @Override
    public void run() {
        System.out.println("Server.run -> started...");
        try {
            new UDPServer(1111, this);
            new TCPServer(PORT, this);
        } catch (SocketException ex) {
            System.out.println(ex.getCause() + " | " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getCause() + " | " + ex.getMessage());
        }
    }

    /*#######################################################################*/
    public static void main(String[] args) {
        Server server = new Server();
    }

}
