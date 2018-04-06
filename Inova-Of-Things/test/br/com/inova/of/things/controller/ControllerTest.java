/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.controller;

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
        Client c = new Client("rua sao caetano 57", "leste");
        Controller instance = new Controller();
        instance.registerNewClient(c);
        Assert.assertEquals(instance.getClient(c.toString()), c);
    }

    /**
     * Test of removeClient method, of class Controller.
     */
    @Test
    public void testRemoveClient() throws Exception {
        System.out.println("removeClient");
        Client c = new Client("rua sao caetano 57", "leste");
        Controller instance = new Controller();
        instance.removeClient(c);
        // TODO review the generated test code and remove the default call to fail.
        Assert.assertEquals(instance.getClient(c.toString()),null);
    }

}
