/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

import br.com.inova.of.things.controller.Controller;
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
 *
 * @author luciano
 */
public class Client extends Application {

    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
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

    private void go(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(15);

        Scene scene = new Scene(grid, 250, 275);
        grid.setPadding(new Insets(25, 25, 25, 25));
        stage.setScene(scene);
        stage.show();

    }

}
