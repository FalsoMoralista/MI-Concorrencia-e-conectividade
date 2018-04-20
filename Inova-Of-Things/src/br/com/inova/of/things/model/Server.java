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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * If this class loading files change directory the server will be started as a
 * brand new.
 *
 * @author luciano
 */
public class Server implements ISubject, Runnable, IObserver {

    private static final int PORT = 8888;
    private Thread thread;
    private HashMap<String, Client> clients = new HashMap<>();
    private HashMap<String, WaterFlowMeasurer> devices = new HashMap<>();
    private HashMap<String, LinkedList<ClientMeasure>> consumptions;

    public Server() {
        this.clients = new HashMap<>();
        this.devices = new HashMap<>();
        this.consumptions = new HashMap<>();
        new File("server").mkdir();
        new File("server/bin").mkdir();
        try {
            this.load();
        } catch (FileNotFoundException | ClassNotFoundException ex) {
            System.out.println("empty records, Server -> fresh starting");
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
            this.consumptions.remove(retrieve.toString(), this.consumptions.get(retrieve.toString()));
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
    // TODO
    @Override
    public void update() {

    }

    @Override
    public Observer getObserver(String key) throws ClientMeasurerNotFoundException {
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
    /**
     * Loads the server state.
     *
     */
    private void load() throws FileNotFoundException, IOException, ClassNotFoundException {
        System.out.println("Server.loading...");
        // tcp (clients)
        FileInputStream instream = new FileInputStream(new File("server/bin/tcp.bin"));
        ObjectInputStream ois = new ObjectInputStream(instream);
        this.clients = (HashMap<String, Client>) ois.readObject();
        // udp (devices)
        instream = new FileInputStream(new File("server/bin/udp.bin"));
        ois = new ObjectInputStream(instream);
        this.devices = (HashMap<String, WaterFlowMeasurer>) ois.readObject();

        instream = new FileInputStream(new File("server/bin/register.bin"));
        ois = new ObjectInputStream(instream);
        this.consumptions = (HashMap<String, LinkedList<ClientMeasure>>) ois.readObject();
        System.out.println("Done");
    }

    /**
     * Saves the current "progress" of the server.
     */
    private void save() throws FileNotFoundException, IOException {
        System.out.println("Server.saving...");
        File f = null;
        // tcp (clients)
        f = new File("server/bin/tcp.bin");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream stream = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(this.clients);
        // udp (devices)
        f = new File("server/bin/udp.bin");
        if (!f.exists()) {
            f.createNewFile();
        }
        stream = new FileOutputStream(f);
        oos = new ObjectOutputStream(stream);
        oos.writeObject(this.devices);
        // register
        f = new File("server/bin/register.bin");
        if (!f.exists()) {
            f.createNewFile();
        }
        stream = new FileOutputStream(f);
        oos = new ObjectOutputStream(stream);
        oos.writeObject(this.consumptions);
        System.out.println("Done");
    }

    /**
     * Write down a reading from a client's WaterFlowMeasurer.
     *
     * @param clientKey (client measure)
     * @param reading
     * @see ClientMeasure
     * @see WaterFlowMeasurer
     */
    public void write(String clientKey, ClientMeasure reading) {
        System.out.println("Server.writing -> client info");
        try {
            List<ClientMeasure> clientRecord = this.consumptions.get(clientKey);
            clientRecord.add(reading);
        } catch (NullPointerException ex) {
            this.consumptions.put(clientKey, new LinkedList<>());
            this.consumptions.get(clientKey).add(reading);
        }
        try {
            this.save();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* ################### CLIENT ##################*/
    public ClientRecord getClientRecord(String key) {
        Client c = this.clients.get(key);
        System.out.println(c);
        return new ClientRecord(c.getEmail(), this.getTotalConsumed(c.toString()), this.consumptions.get(c.toString()));
    }

    /**
     *
     * @param key the client measurer to string
     * @return
     */
    public double getTotalConsumed(String key) {
        Double[] total = new Double[1];
        List<ClientMeasure> clientRecord = this.consumptions.get(key);
        List<ClientMeasure> diff = new LinkedList<>();
        clientRecord.forEach(m -> {
            if (!diff.contains(m)) {
                diff.add(m);
            }
        });
        diff.forEach(m -> total[0] = m.getReading());
        return total[0];
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
        System.out.println(server.getClientRecord("lucianoadfilho@gmail.com"));
        
    }

}
