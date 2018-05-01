/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.server.tcp;

import br.ecomp.uefs.server.Server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listen to connections through TCP protocol.
 * Waits for connections then create multiple threads to handle requests.
 * @see TCPThread
 * @author luciano
 */
public class TCPListener implements Runnable {

    private static final int PORT = 8888;
    private ServerSocket socket;
    private Server server;

    public TCPListener(Server server) {
        new Thread(this).start();
        this.server = server;
    }

    @Override
    public void run() {
        try {
            socket = new ServerSocket(PORT); // bind a socket to the port address
            this.listen(); // start to listen for connections
        } catch (IOException ex) {
            Logger.getLogger(TCPListener.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     *  Listen for connection requests. 
     */
    private void listen() throws IOException{
        Socket s;
        System.out.println("Server.listening to the port -> "+PORT+" via TCP...");
        while(true){ // listen for connections then start threads to attend requests from the connections
            s = socket.accept();
            TCPThread thread = new TCPThread(s,server);
            thread.start();
        }
    }

}
