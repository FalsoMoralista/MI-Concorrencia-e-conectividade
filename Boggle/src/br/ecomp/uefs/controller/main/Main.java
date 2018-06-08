/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.controller.main;

import br.ecomp.uefs.controller.Controller;
import br.ecomp.uefs.view.LoginWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * This class should be used by a administrator.
 * @author luciano
 */
public class Main extends Application {

    protected Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("     Boggle");
        this.before(primaryStage);
    }

    private void before(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(0);

        Button ok = new Button("Ok");
        Label hostname = new Label("Server hostname/ip:");
        TextField get = new TextField();

        grid.add(get, 1, 0);
        grid.add(hostname, 0, 0);
        grid.add(ok, 2, 0);

        ok.setOnAction(e -> {
            String text = get.getText();
            if (text.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("missing information");
                alert.show();
            } else {
                this.controller = new Controller(text);
                this.go(stage);
            }
        });
        Scene scene = new Scene(grid, 425, 125);
        grid.setPadding(new Insets(25, 25, 25, 25));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }

    private void go(Stage stage) {        
        LoginWindow login = new LoginWindow(controller);
        login.start(stage);
    }
}
