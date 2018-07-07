/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sdcom.view.StoreScreen;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Main extends Application {

    private String NAME;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        before(primaryStage);
    }

    private void before(Stage stage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(0);

        Button ok = new Button("Ok");
        Label hostname = new Label("Store name:");
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
                NAME = text;
                try {
                    this.go(stage);
                } catch (NotBoundException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(ex.getMessage());
                    alert.show();
                }
            }
        });
        Scene scene = new Scene(grid, 425, 125);
        grid.setPadding(new Insets(25, 25, 25, 25));
        stage.setScene(scene);
        stage.show();
    }

    private void go(Stage stage) throws IOException, FileNotFoundException, RemoteException, NotBoundException, Exception {
        StoreScreen store = new StoreScreen(NAME);
        store.start(stage);
    }

}
