/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.of.things.view;

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

    private volatile static double waterFlow = 0;

    private Boolean flowing = false;
    private Thread task;

    @Override
    public void start(Stage primaryStage) throws Exception {

        GridPane grid = new GridPane();
        this.setUp(primaryStage, grid);

        List<Gap<Integer, Double>> measures = new LinkedList();

        status.setText(" Currently stopped");
        status.setFill(Color.FIREBRICK);
        long[] read = {0};
        start.setOnAction(e -> {
            read[0] = System.currentTimeMillis();
            waterFlow = 0;
            flowing = true;
            status.setText(" Flowing");
            status.setFill(Color.GREEN);
            flow.setText("Water flow: " + waterFlow);
            start.setVisible(false);
            stop.setVisible(true);
//            task = new Thread(this.startConsume());
//            task.start();
        });

        stop.setOnAction(e -> {

            flowing = false;
            waterFlow = 0;

            status.setText(" Currently stopped");
            status.setFill(Color.FIREBRICK);

            flow.setText("Water flow:");

            start.setVisible(true);
            stop.setVisible(false);

            double[] sum = {0};
            for(Gap<Integer,Double> g : measures){
                sum[0] += g.getMeasure()*g.getTime();
            }
            System.out.println(sum[0]);
        });

        more.setOnAction(e -> {
            if (flowing) {
                long actual = System.currentTimeMillis();
                measures.add(new Gap(String.format("%.2f",(read[0]-actual)/1000),waterFlow));
                read[0] = actual;
                waterFlow += 0.5;
                flow.setText("Water flow: " + waterFlow);
            }
        });

        less.setOnAction(e -> {
            if (flowing) {
                long actual = System.currentTimeMillis();
                measures.add(new Gap(String.format("%.2f",(read[0]-actual)/1000),waterFlow));
                read[0] = actual;
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
        primaryStage.setResizable(false);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(15);
        grid.autosize();
        Scene scene = new Scene(grid, 350, 225);
        grid.setPadding(new Insets(20, 20, 20, 20));
        primaryStage.setScene(scene);
    }

//    private Runnable startConsume() {
//        int[] iarr = {0};
//        double[] val = {0};
//        List<Gap<Long, Double>> measures = new LinkedList();
//        Runnable task = () -> {
//            while (flowing) {
//                if (iarr[0] % 1000000000 == 1) {
//                    val[0] += waterFlow;
//                    System.out.println(val[0]);
//                }
//                iarr[0]++;
//            }
//            System.out.println("Consumed = " + val[0]);
//        };
//        return task;
//    }
//
    public void bind(WaterFlowMeasurer measurer) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
