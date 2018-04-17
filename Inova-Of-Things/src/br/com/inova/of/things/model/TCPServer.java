/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

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
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class TCPServer extends Subject implements Runnable {

    private int port;

    private Socket socket;
    private ServerSocket serverSocket;

    private HashMap<String, Client> clients = new HashMap<>();

    private boolean listening;

    public TCPServer(int port) throws IOException {
        super();
        try {
            this.load();
            System.out.println(clients.get("lucianoadfilho@gmail.com"));
        } catch (FileNotFoundException ex) {
            File f = new File("src/br/com/inova/of/things/server/bin");
            f.mkdir();
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        this.startListen();
        new Thread(this).start();
    }

    public void startListen() {
        this.listening = true;
    }

    public void stopListening() {
        this.listening = false;
    }

    @Override
    public void run() {
        System.out.println("Server.started listening to the port -> " + port + " via TCP...");
        while (listening) {
            try {
                socket = serverSocket.accept();
                System.out.println("Message received from -> " + socket.getInetAddress().toString() + " via TCP");
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                // parse
                String message = br.readLine();
                if (message.startsWith("1")) {
                    this.registrate(message);
                }
                // send message to client
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("message received -> " + message);
                bw.flush();
                is.close();
                os.close();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void registrate(String clInfo) {
        String email = clInfo.substring(clInfo.indexOf("|") + 1, clInfo.indexOf("-"));
        String address = clInfo.substring(clInfo.indexOf("-") + 1, clInfo.indexOf(":"));
        String zone = clInfo.substring(clInfo.indexOf(":") + 1, clInfo.indexOf(";"));
        try {
            this.clients.get(email).getEmail().toString();
            // throw back exception here. (already registered)
        } catch (NullPointerException ex) {
            clients.put(email, new Client(email, address, zone));
            
            try {
                this.save();
            } catch (IOException ex1) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    private void load() throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream instream = new FileInputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectInputStream ois = new ObjectInputStream(instream);
        this.clients = (HashMap<String, Client>) ois.readObject();
    }

    private void save() throws FileNotFoundException, IOException {
        FileOutputStream stream = new FileOutputStream(new File("src/br/com/inova/of/things/server/bin/tcp.bin"));
        ObjectOutputStream oos = new ObjectOutputStream(stream);
        oos.writeObject(this.clients);
    }
}
