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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luciano
 */
public class Server implements Runnable{
    
    private Socket socket;
    private ServerSocket serverSocket;
    private static final int PORT = 25000;
    private Thread thread;
    
    public Server(){
        try {        
            
            InetAddress address = InetAddress.getByName("myserver");
            serverSocket = new ServerSocket(PORT);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread = new Thread(this);
        thread.start();
    }
    
    public Socket getSocket(){
        return this.socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server started listening to the port "+PORT);
            while(true){
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                System.out.println("Client's message: "+message);
                // send message to client
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write("message received -> "+message);
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {                   
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }           
    
    public static void main(String[] args){
        Server myServer = new Server();
    }
}
