/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import br.ecomp.uefs.controller.Controller;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import shared.exception.InvalidPasswordException;
import shared.exception.InvalidTypeOfRequestException;
import shared.model.User;

/**
 *
 * @author luciano
 */
public class LoginWindow extends Application implements Serializable {

    private Button signIn;
    private Button signUp;

    private TextField emailField;
    private PasswordField passwordField;

    private Label email;
    private Label password;

    private Controller controller = new Controller("localhost");

    private String user;
    private String pw;

    private boolean succeded;
    private boolean ok;

    private static final String PATH = "src/br/uefs/ecomp/roadTrips/bin/sv.bin";

    private final Text authError = new Text();

    public LoginWindow(Controller controller) {
        this.controller = controller;
    }

    public LoginWindow() {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(15);
        grid.autosize();
        grid.add(authError, 2, 4);
        Scene scene = new Scene(grid, 800, 600);
        grid.setPadding(new Insets(20, 20, 20, 20));

        Text sceneTitle = new Text("Boggle");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        grid.add(sceneTitle, 2, 0);

        email = new Label("User name:");
        email.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        grid.add(email, 1, 1);
        password = new Label("Password:     ");
        password.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        grid.add(password, 1, 2);

        emailField = new TextField();

        grid.add(emailField, 2, 1);

        passwordField = new PasswordField();
        grid.add(passwordField, 2, 2);
        signIn = new Button("Sign in");
        signIn.setStyle("-fx-background-color: lightskyblue; -fx-text-fill: black;");
        signIn.setOnAction((ActionEvent event) -> {

            if (!emailField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
                try {
                    user = emailField.getText();
                    pw = passwordField.getText();
                    succeded = controller.authenticate(user, pw);
                } catch (NullPointerException e) {
                } catch (IOException ex) {
                    Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidPasswordException ex) {
                    Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InvalidTypeOfRequestException ex) {
                    Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (!succeded) {
                authError.setFill(Color.CRIMSON);
                authError.setText("Invalid user name or password");
            } else {
                try {
                    login(controller.getInstance(), primaryStage);
                } catch (Exception ex) {
                    Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        HBox hbSignIn = new HBox(5);
        hbSignIn.setAlignment(Pos.CENTER_RIGHT);
        hbSignIn.getChildren().add(signIn);

        signUp = new Button("Sign up");
        signUp.setOnAction((ActionEvent event) -> {
            Registration register = new Registration(controller);
            try {
                emailField.clear();
                passwordField.clear();
                register.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox hbSignUp = new HBox(5);
        hbSignUp.setAlignment(Pos.CENTER_LEFT);
        hbSignUp.getChildren().add(signUp);

        grid.add(hbSignIn, 2, 3);

        grid.add(hbSignUp, 1, 3);

        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pw;
    }

    private void login(User u, Stage primaryStage) throws Exception {

        Alert sucessfull = new Alert(Alert.AlertType.INFORMATION);
        sucessfull.setTitle("Welcome");
        sucessfull.setHeaderText("Welcome" + " " + u.getUsername()+ "!");

        MainMenu main = new MainMenu(controller);
        main.start(primaryStage);
        sucessfull.show();
    }

    /**
     * TODO FIX
     */
    public void load() {
        try {
            FileInputStream restFile = new FileInputStream(PATH);
            ObjectInputStream stream = new ObjectInputStream(restFile);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean handshake() {
        return ok;
    }
}
