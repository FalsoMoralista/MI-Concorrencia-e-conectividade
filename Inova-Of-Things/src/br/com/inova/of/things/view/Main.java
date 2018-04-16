/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

import br.com.inova.of.things.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author luciano
 */
public class Main extends Application{
    protected Controller controller;
    protected Stage primary;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;        
        primary.show();
    }
}
