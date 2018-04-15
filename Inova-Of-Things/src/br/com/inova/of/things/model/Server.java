/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class Server extends Subject implements Runnable {

    private Socket socket;
    private ServerSocket serverSocket;
    private static final int PORT = 8888;
    private Thread thread;

    public Server() {
        super();
        try {
            serverSocket = new ServerSocket(PORT);
            
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            new UDPServer(PORT);
            try {
                System.out.println("Server.started listening to the port -> " + PORT + "...");
                while (true) {
                    socket = serverSocket.accept();
                    System.out.println(socket.getInetAddress().toString());
                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    String message = br.readLine();
                    System.out.println("Client's message: " + message);
                    // send message to client
                    OutputStream os = socket.getOutputStream();
                    OutputStreamWriter osw = new OutputStreamWriter(os);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write("message received -> " + message);
                    bw.flush();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch(NullPointerException ex){
                
            } finally {
                try {
                    socket.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void attach(Observer observer) {
        System.out.println("Server.attaching -> " + observer.toString());
        super.attach(observer); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void detach(Observer observer) {
        System.out.println("Server.detaching -> " + observer.toString());
        super.detach(observer); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Observer getObserver(String key) {
        return super.getObserver(key);
    }
    
    public static void main(String[] args){
        Server server = new Server();
    }
}
