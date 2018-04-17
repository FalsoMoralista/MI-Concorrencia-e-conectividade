/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.interfaces.IObserver;
import br.com.inova.of.things.interfaces.ISubject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
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
        new File("src/br/com/inova/of/things/server").mkdir();
        new File("src/br/com/inova/of/things/server/bin").mkdir();
        try {
            this.load();
            this.remove(this.getClient("lucianoadfilho@gmail.com"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientAlreadyRemovedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            new UDPServer(1111, this);
            new TCPServer(PORT, this);
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Client getClient(String key) {
        return this.clients.get(key);
    }

    public void registrate(Client c) throws ClientAlreadyRegisteredException {
        try {
            Client retrieve = this.clients.get(c.toString());
            throw new ClientAlreadyRegisteredException();
        } catch (NullPointerException ex) {
            this.clients.put(c.toString(), c);
            this.attach(new WaterFlowMeasurer(c.toString()));
            try {
                this.save();
            } catch (IOException exc) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, exc);
            }
        }
    }

    public void remove(Client c) throws ClientAlreadyRemovedException {
        Client retrieve = this.clients.get(c.toString());
        if (retrieve == null) {
            throw new ClientAlreadyRemovedException();
        } else {
            this.detach(this.getObserver(c.toString()));
            this.clients.remove(c, c.toString());
            try {
                this.save();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void Update() {
        // todo
    }

    public Observer getObserver(String key) {
        return this.devices.get(key);
    }

    @Override
    public void attach(Observer observer) {
        this.devices.put(observer.toString(), (WaterFlowMeasurer) observer);
    }

    @Override
    public void detach(Observer observer) {
        this.devices.remove(observer.toString(), observer);
    }

    private void load() throws FileNotFoundException, IOException, ClassNotFoundException {
        // tcp (clients)
        FileInputStream instream = new FileInputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectInputStream ois = new ObjectInputStream(instream);
        this.clients = (HashMap<String, Client>) ois.readObject();
        // udp (devices)
        instream = new FileInputStream(new File("src/br/com/inova/of/things/server/bin/udp.bin"));
        ois = new ObjectInputStream(instream);
        this.devices = (HashMap<String, WaterFlowMeasurer>) ois.readObject();
    }

    private void save() throws FileNotFoundException, IOException {
        // tcp (clients)
        FileOutputStream stream = new FileOutputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(this.clients);
        // udp (devices)
        stream = new FileOutputStream(new File("src/br/com/inova/of/things/server/bin/udp.bin"));
        oos = new ObjectOutputStream(stream);
        oos.writeObject(this.devices);
    }
}
