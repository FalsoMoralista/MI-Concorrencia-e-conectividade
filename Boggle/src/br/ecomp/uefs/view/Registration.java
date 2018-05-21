/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import br.ecomp.uefs.controller.Controller;
import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shared.exception.InvalidDataException;
import shared.exception.InvalidTypeOfRequestException;
import shared.exception.UserAlreadyRegisteredException;
import br.ecomp.uefs.model.User;

/**
 *
 * @author luciano
 */
public class Registration extends Application implements Serializable {

    private TextField loginField;
    private PasswordField passwordField;

    private Label passwordLabel;
    private Label nameLabel;

    private Button submit;
    private Button cancel;

    private Controller controller = new Controller("localhost");

    protected User user;

    private final Text infoError = new Text();

    public static void main(String[] args) {
        launch(args);
    }

    public Registration() {
        
    }
    
    public Registration(Controller controller) {
        this.controller = new Controller("localhost");
    }

    public User getInfo() throws InvalidDataException, IOException {
        if (passwordField.getText().isEmpty() && loginField.getText().isEmpty()) {
            throw new InvalidDataException();
        }
        return user = new User(loginField.getText(), passwordField.getText());
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        Scene scene = new Scene(grid, 500, 400);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label header = new Label("Registration");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        grid.add(header, 1, 0, 2, 1);

        nameLabel = new Label("User name:");
        grid.add(nameLabel, 0, 1);
        
        loginField = new TextField();
        grid.add(loginField,1,1);

        passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 3);

        grid.add(infoError, 1, 5);
        submit = new Button("Submit");
        submit.setOnAction((ActionEvent event) -> {
            try {
                Alert error = new Alert(Alert.AlertType.ERROR);
                getInfo();
                try {
                    controller.register(user);
                    Alert ok = new Alert(Alert.AlertType.INFORMATION);
                    ok.setTitle("Registration sucessfull");
                    ok.setHeaderText("You were sucessfully registered");
                    Optional<ButtonType> result = ok.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        primaryStage.close();
                    } else {
                        primaryStage.close();
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UserAlreadyRegisteredException ex) {
                    error.setTitle("Error");
                    error.setHeaderText("User name already in use");
                    error.show();
                } catch (InvalidTypeOfRequestException ex) {
                    Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InvalidDataException ide) {
                infoError.setFill(Color.FIREBRICK);
                infoError.setText("Missing information");
            } catch (IOException ex) {
                Logger.getLogger(Registration.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        HBox hbSubmit = new HBox(10);
        hbSubmit.setAlignment(Pos.CENTER_RIGHT);
        hbSubmit.getChildren().add(submit);
        grid.add(hbSubmit, 3, 6);

        cancel = new Button("Cancel");
        cancel.setOnAction((ActionEvent event) -> {
            primaryStage.close();
        });

        HBox hbCancel = new HBox(10);
        hbCancel.setAlignment(Pos.CENTER);
        hbCancel.getChildren().add(cancel);
        grid.add(hbCancel, 0, 6);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
