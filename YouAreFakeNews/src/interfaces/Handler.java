/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.io.IOException;
import org.jgroups.Message;

/**
 *
 * @author Luciano Araujo Dourado Filho <ladfilho@gmail.com>
 */
public interface Handler {
    public void handle(Message message) throws IOException, Exception;
}
