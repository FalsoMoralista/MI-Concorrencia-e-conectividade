/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.controller;

import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.exceptions.ClientAlreadyRemovedException;
import br.com.inova.of.things.exceptions.ClientNotFoundException;
import br.com.inova.of.things.model.Client;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author luciano
 */
public class ControllerTest {

    private static final Controller instance = new Controller();
    private static final Client c = new Client("rua augusta 38", "leste");

    
    
    public ControllerTest() {

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of registerNewClient method, of class Controller.
     */
    @Test
    public void testRegisterNewClient() throws Exception {
        System.out.println("registerNewClient");
        instance.registerNewClient(c);
        Assert.assertEquals(instance.getClient(c.toString()), c);
    }

    /**
     * Test of removeClient method, of class Controller.
     */
    @Test(expected = ClientNotFoundException.class)
    public void testRemoveClient() throws ClientAlreadyRemovedException, ClientNotFoundException {
        System.out.println("removeClient");
        instance.removeClient(c);
        instance.getClient(c.toString());
        // TODO review the generated test code and remove the default call to fail.
    }
    
    @Test
    public void testGetClient() throws ClientAlreadyRegisteredException, ClientNotFoundException{
        System.out.println("getClient");
        instance.registerNewClient(c);
        Client test = instance.getClient(c.toString());
        Assert.assertEquals(test,c);
    }

}
