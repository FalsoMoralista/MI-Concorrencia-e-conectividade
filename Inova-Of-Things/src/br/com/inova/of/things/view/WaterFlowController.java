/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author luciano
 */
public class WaterFlowController extends Application {

    private Button start = new Button("Start");
    private Button stop = new Button("Stop");

    private Button more = new Button("+");
    private Button less = new Button("-");

    private Text flow = new Text("Water flow: ");
    private Text status = new Text();
    private double waterFlow = 0;
    private Boolean flowing = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();
        this.setUp(primaryStage, grid);
        status.setText(" Currently stopped");
        status.setFill(Color.FIREBRICK);
        start.setOnAction(e -> {
            waterFlow = 0;
            flowing = true;
            status.setText(" Flowing");
            status.setFill(Color.GREEN);
            flow.setText("Water flow: " + waterFlow);
            start.setVisible(false);
            stop.setVisible(true);
        });
        stop.setOnAction(e -> {
            waterFlow = 0;
            flowing = false;
            status.setText(" Currently stopped");
            status.setFill(Color.FIREBRICK);
            flow.setText("Water flow:");
            start.setVisible(true);
            stop.setVisible(false);
        });
        more.setOnAction(e -> {
            if (flowing) {
                waterFlow += 0.5;
                flow.setText("Water flow: " + waterFlow);
            }
        });
        less.setOnAction(e -> {
            if (flowing) {
                waterFlow -= 0.5;
                flow.setText("Water flow: " + waterFlow);
            }
        });

        grid.add(start, 0, 1);
        grid.add(stop, 0, 1);
        stop.setVisible(false);
        grid.add(more, 1, 1);
        grid.add(less, 2, 1);

        grid.add(flow, 0, 0);
        grid.add(status, 1, 0);
        primaryStage.show();
    }

    private void setUp(Stage primaryStage, GridPane grid) {
        primaryStage.setTitle("Water flow controller");
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(15);
        grid.autosize();
        Scene scene = new Scene(grid, 350, 225);
        grid.setPadding(new Insets(20, 20, 20, 20));
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
