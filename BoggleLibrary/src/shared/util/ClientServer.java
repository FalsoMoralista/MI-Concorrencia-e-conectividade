/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class should be used to send messages between clients and servers.
 * It it's actual version supports TCP & UDP protocols.
 * @author luciano
 */
public class ClientServer {

    public ClientServer() {
    }

    /**
     * Send an object as request to a server.
     *
     * @param host
     * @param port
     * @param request
     * @return
     * @throws java.net.UnknownHostException
     */
    public static final Object sendTCP(String host, int port, Object request) throws UnknownHostException, IOException, ClassNotFoundException {
        Socket clientSocket = new Socket(InetAddress.getByName(host), port);
        ObjectOutputStream outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        outToServer.writeObject(request); // send the request
        Object retrieved = inFromServer.readObject();
        clientSocket.close();
        return retrieved;
    }

    /**
     *  Send an object as request via UDP.
     * @param host
     * @param port
     * @param request
     * @throws java.net.UnknownHostException
     * @throws java.net.SocketException
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static final void sendUDP(String host, int port, Object request) throws UnknownHostException, SocketException, IOException, ClassNotFoundException {
        DatagramSocket socket = new DatagramSocket();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(outputStream);
        os.writeObject(request);
        byte[] data = outputStream.toByteArray();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(host), port);
        socket.send(sendPacket);
    }

    public static void main(String[] args) throws IOException {
    }
}
