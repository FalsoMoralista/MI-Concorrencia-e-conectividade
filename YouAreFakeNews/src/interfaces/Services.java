package interfaces;

import br.com.inova.exception.NoAverageException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Properties;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Defines the remote methods.
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public interface Services extends Remote {

    public void rateNews(int newsID, int rate) throws IOException, RemoteException;
    public int getTrunkAVG(int newsID)throws RemoteException, NoAverageException, IOException;
    public Properties getNews()throws RemoteException;
}
