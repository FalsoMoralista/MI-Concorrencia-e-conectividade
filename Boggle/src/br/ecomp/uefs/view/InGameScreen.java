/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import shared.model.GameDices;

/**
 * The main game screen
 *
 * @author Luciano Araujo Dourado Filho
 */
public class InGameScreen extends Application {

    private Button[][] buttons;

    public InGameScreen() {
        dices = new GameDices();
        buttons = new Button[4][4];
    }

    private GameDices dices;

    /**
     * Set up screen properties.
     */
    private void setUp(Stage primaryStage) {
        primaryStage.setTitle("Boggle");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(15);
        grid.autosize();
        grid.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Initialize all buttons.
     */
    public void initializeButtons() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.setUp(primaryStage);
        this.initializeButtons();
        this.shuffleDices();
        this.randomizeDices();
        GridPane grid = (GridPane) primaryStage.getScene().getRoot();

        TilePane pane = new TilePane();
        pane.setPrefColumns(4);
        pane.setPrefRows(4);
        pane.setVgap(10);
        pane.setHgap(10);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                pane.getChildren().add(buttons[i][j]);
            }
        }
        
        grid.add(pane, 0, 0);
        primaryStage.show();
    }

    /**
     * Shuffles all the 16 dices.
     *
     * @see GameDices
     */
    public void shuffleDices() {
        List<String> row = new LinkedList<>();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 6; j++) {
                row.add(dices.DICES[i][j]); // get all letters from a dice and
            }                               // adds it to a list
            Collections.shuffle(row);       // then shuffles it.
            int j = 0;
            for (String letter : row) {
                dices.DICES[i][j++] = letter;
            }
            row.clear();
        }
    }

    /**
     * Generate the random letters for the dices.
     */
    public void randomizeDices() {
        int minimum = 0;
        int maximum = 5;
        Random rn = new Random();
        int n = maximum - minimum + 1;

        String[] randomized = new String[16];
        for (int row = 0; row < 16; row++) {
            int randomNum = rn.nextInt() % n;
            int column = Math.abs(minimum + randomNum);
            randomized[row] = dices.DICES[row][column];
        }

        int get = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j].setText(randomized[get++]);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
