/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class ClientServer implements Runnable {

    private Socket socket;
    private Thread t;
    private static String host;
    private static int port;
    public ClientServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {            
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            // sent a message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);

            String message = "Hello World!\n";
            bw.write(message);
            bw.flush();

            System.out.println("Message sent to server -> " + message);
            // get the return message
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            message = br.readLine();
            System.out.println("Message received from the server : " + message);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void sendMessage(String msg) throws SocketException, IOException{
        DatagramSocket s = new DatagramSocket();
        DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName(host), 8888);        
        s.send(p);
        System.out.println("client.message sent -> "+msg);
        p = new DatagramPacket(new byte[1024], new byte[1024].length);
        s.receive(p);
        String received = new String(p.getData());
        System.out.println("client.received message -> "+received);
    }
    
    public static void main(String[] args) throws IOException {
        ClientServer c = new ClientServer("localhost",8888);
        c.sendMessage("hello");
    }
}
