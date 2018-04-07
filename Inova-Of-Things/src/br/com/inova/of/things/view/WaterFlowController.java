/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

import br.com.inova.of.things.controller.Controller;
import br.com.inova.of.things.model.Gap;
import br.com.inova.of.things.model.WaterFlowMeasurer;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class will be used to simulate a water flow sensor reading flowing
 * measures. After binding it to a sensor(WaterFlowMeasurer) with an ID, the
 * user will be able to simulate the water flow from the sensor, and when
 * satisfied, the total consume will be increased on the chosen sensor's total
 * consume.
 *
 * @see Gap
 * @see WaterFlowMeasurer
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

    private volatile static double waterFlow = 0;

    private Boolean flowing = false;
    private Boolean binded = false;

    private Controller controller;

    private long[] read = {0};
    private List<Gap<Double, Double>> measures;

    public WaterFlowController(Controller controller) {
        this.controller = controller;
    }

    public WaterFlowController() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane grid = new GridPane();

        if (binded) {
            this.setUp(primaryStage, grid);
            measures = new LinkedList();
            this.addButtons(grid);
            this.setButtonsProperties();
            primaryStage.show();
        } else {
            this.before(primaryStage);
        }
    }

    private void setUp(Stage primaryStage, GridPane grid) {
        primaryStage.setTitle("Water flow controller");
        primaryStage.setResizable(false);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(15);
        grid.autosize();
        Scene scene = new Scene(grid, 350, 225);
        grid.setPadding(new Insets(20, 20, 20, 20));
        primaryStage.setScene(scene);
    }

    private void addButtons(GridPane grid) {
        grid.add(start, 0, 1);
        grid.add(stop, 0, 1);
        stop.setVisible(false);
        grid.add(more, 1, 1);
        grid.add(less, 2, 1);
        grid.add(flow, 0, 0);
        grid.add(status, 1, 0);
    }

    private void setButtonsProperties() {
        status.setText(" Currently stopped");
        status.setFill(Color.FIREBRICK);
        start.setOnAction(e -> {
            read[0] = System.currentTimeMillis();
            waterFlow = 0;
            flowing = true;
            status.setText(" Flowing");
            status.setFill(Color.GREEN);
            flow.setText("Water flow: " + waterFlow);
            start.setVisible(false);
            stop.setVisible(true);
        });
        stop.setOnAction(e -> {
            if (flowing) {
                long actual = System.currentTimeMillis();
                measures.add(new Gap(Double.parseDouble(String.format("%.2f", (actual - read[0]) / 1000.0)), waterFlow)); // save the last value of water flow and its gap of time
                flowing = false;
                waterFlow = 0;
                status.setText(" Currently stopped");
                status.setFill(Color.FIREBRICK);

                flow.setText("Water flow:");

                start.setVisible(true);
                stop.setVisible(false);
            }
            double[] sum = {0};
            for (Gap<Double, Double> g : measures) {
                sum[0] += g.getMeasure() * g.getTime();
            }
            System.out.println("Total consumed -> " + sum[0]);
        });
        more.setOnAction(e -> {
            if (flowing) {
                long actual = System.currentTimeMillis();
                System.out.println("Time lap in -> " + String.format("%.2f", (actual - read[0]) / 1000.0));
                measures.add(new Gap(Double.parseDouble(String.format("%.2f", (actual - read[0]) / 1000.0)), waterFlow)); // save the last value of water flow and its gap of time
                read[0] = actual;
                waterFlow += 0.5;
                flow.setText("Water flow: " + waterFlow);
            }
        });
        less.setOnAction(e -> {
            if (flowing) {
                long actual = System.currentTimeMillis();
                System.out.println("Time lap in -> " + String.format("%.2f", (read[0] - actual) / 1000.0 / 3600.0));
                measures.add(new Gap(Double.parseDouble(String.format("%.2f", (actual - read[0]) / 1000.0)), waterFlow)); // save the last value of water flow and its gap of time
                read[0] = actual;
                waterFlow -= 0.5;
                flow.setText("Water flow: " + waterFlow);
            }
        });
    }

    private boolean bind(WaterFlowMeasurer measurer) {
        return false;
    }

    private void before(Stage st) {
        GridPane grid = new GridPane();
        st.setTitle("Water bind control");
        st.setResizable(false);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(7);
        grid.setVgap(15);
        grid.autosize();

        Label label = new Label("Client ID ");
        TextField idGetter = new TextField();
        Button ok = new Button("Ok");

        Text img = new Text("       image on center");
        img.setFill(Color.RED);
        Text fix = new Text("");

        grid.add(label, 0, 1);
        grid.add(idGetter, 1, 1);
        grid.add(ok, 2, 1);
        grid.add(fix, 1, 2);
        grid.add(img, 1, 0);

        ok.setOnAction(a -> {
            if (!idGetter.getText().isEmpty()) {
               // TODO call controller and try to get the client to bind it
            }else{
                System.out.println("show errror msg");
            }
        });

        Scene scene = new Scene(grid, 340, 205);
        grid.setPadding(new Insets(20, 20, 20, 20));
        st.setScene(scene);
        st.show();
        WaterFlowMeasurer measurer = null;
        this.bind(measurer);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
