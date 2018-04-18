/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

import br.com.inova.of.things.controller.Controller;
import br.com.inova.of.things.exceptions.ClientAlreadyRegisteredException;
import br.com.inova.of.things.model.Client;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author luciano
 */
public class RegistrateForm extends Application {

    private Text email = new Text("     e-mail");
    private Text address = new Text("       address");
    private Text zone = new Text("      zone");
    private Text alert = new Text();

    private Button submit = new Button("submit");
    private Button cancel = new Button("cancel");

    private TextField getEmail = new TextField();
    private TextField getAddress = new TextField();
    private TextField getZone = new TextField();

    private Controller controller = new Controller();

    public RegistrateForm() {
    }

    public RegistrateForm(Controller c) {
        this.controller = c;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.setUp(primaryStage);
        this.setButtonsProperties();
        primaryStage.show();
        cancel.setOnAction(e -> primaryStage.close());
    }

    private void setUp(Stage primaryStage) {
        primaryStage.setTitle("Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        grid.add(email, 1, 0);
        grid.add(getEmail, 1, 1);

        grid.add(address, 2, 0);
        grid.add(getAddress, 2, 1);

        grid.add(zone, 3, 0);
        grid.add(getZone, 3, 1);

        grid.add(cancel, 1, 2);
        grid.add(alert, 1, 3);
        grid.add(submit, 3, 2);

        alert.setVisible(false);
        alert.setText("missing information");
        alert.setFill(Color.FIREBRICK);

        Scene scene = new Scene(grid, 500, 350);
        grid.setPadding(new Insets(25, 25, 25, 25));
        primaryStage.setScene(scene);
    }

    private void setButtonsProperties() {
        this.submit.setOnAction(e -> {
            if (!getEmail.getText().isEmpty() && !getAddress.getText().isEmpty() && !getZone.getText().isEmpty()) {
                alert.setVisible(false);
                try {
                    controller.registerNewClient(new Client(getEmail.getText(), getAddress.getText(),getZone.getText()));
                } catch (IOException ex) {
                    Logger.getLogger(RegistrateForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RegistrateForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                alert.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
