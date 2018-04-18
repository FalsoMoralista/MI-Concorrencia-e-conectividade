/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
public class ClientServer {

    public ClientServer() {
    }
        
    public static final void sendTCP(String host, int port, String msg) {
        Socket socket = null;        
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);

            // sent a message to the server
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(msg);
            bw.flush();
            System.out.println("Message sent to server -> " + msg);
//            // get the return message
//            InputStream is = socket.getInputStream();
//            InputStreamReader isr = new InputStreamReader(is);
//            BufferedReader br = new BufferedReader(isr);
//            msg = br.readLine();
//            System.out.println("Message received from the server : " + msg);
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

    /**
     * Send a message through UDP connection to a server.
     *
     * @param host
     * @param port
     * @param msg
     * @throws java.net.SocketException
     */
    public static final void sendUDP(String host, int port, String msg) throws SocketException, IOException {
        DatagramSocket s = new DatagramSocket();
        DatagramPacket p = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName(host), port);
        s.send(p);
        System.out.println("client.message sent -> " + msg);
        p = new DatagramPacket(new byte[1024], new byte[1024].length);
        s.receive(p);
        String received = new String(p.getData());
        System.out.println("client.received message -> " + received);
    }
        
    
    /**
     *  Send an object as request to a server.
     * 
     * @param host
     * @param port
     * @param request
     * @return 
     * @throws java.net.UnknownHostException
     */
    public static final Object request(String host,int port,Object request) throws UnknownHostException, IOException, ClassNotFoundException{
        Socket clientSocket = new Socket(InetAddress.getByName(host),port);
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        outToServer.writeObject(request); // send the request
        Object retrieved = inFromServer.readObject();
        clientSocket.close();
        return retrieved;
    }

    public static void main(String[] args) throws IOException {
    }
}
