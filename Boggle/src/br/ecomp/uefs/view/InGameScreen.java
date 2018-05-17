/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import shared.model.Game;
import shared.model.GameDices;
import shared.model.GameSession;

/**
 * The main game screen
 *
 * @author Luciano Araujo Dourado Filho
 */
public class InGameScreen extends Application {

    GameSession session;
    Game game;
    
    private Button[][] buttons = new Button[4][4];
    private TextField textField = new TextField();
    private Button submit = new Button("submit");
    private Button clear = new Button("clear");
    private Button x = new Button("x");
    private LinkedList<String> myWords = new LinkedList<>();
    private LinkedList<String> letterSet = new LinkedList<>();
    private ListView seeWords = new ListView();

    public InGameScreen(Game game) throws IOException {
        this.game = game;
        dices = new GameDices();
        session = new GameSession();
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
        grid.setVgap(10);
        grid.autosize();
        grid.add(textField, 0, 1);
        grid.add(submit, 1, 2);
        grid.add(clear, 2, 2);
        grid.add(x, 0, 2);
        grid.add(new Label("My words"), 6, 0);
        grid.add(seeWords, 6, 1);
        grid.setPadding(new Insets(20, 20, 20, 20));
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Initialize all dices.
     */
    public void initializeDices() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
            }
        }
    }

    /**
     * Set all button actions.
     */
    private void setActions() {
        LinkedList<String> list = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String btnName = buttons[i][j].getText();
                buttons[i][j].setOnAction(e -> {
                    if (letterSet.contains(btnName)) {
                        letterSet.remove(btnName);
                        textField.setText(textField.getText() + btnName);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("No letters remaining, all letters available already added");
                        alert.show();
                    }
                });
            }
        }
        clear.setOnAction(e -> {
            String txt = textField.getText();
            int j = 1;
            for (int i = 0; i < txt.length(); i++) {
                letterSet.add(txt.substring(i, j++));
            }
            textField.clear();
            
        });
        submit.setOnAction(e -> {

            Alert alert = null;

            String txt = textField.getText();

            if (!txt.isEmpty()) {

                if (txt.length() > 2) {

                    if (!myWords.contains(txt)) {
                        if (verifyWord(txt)) {
                            myWords.add(txt);
                            seeWords.setItems(new ObservableListWrapper(myWords));
                        }else{
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Not a word");
                            alert.show();
                        }

                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Word already added");
                        alert.show();
                    }
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Not a valid word");
                    alert.show();
                }
            }
            int j = 1;
            for (int i = 0; i < txt.length(); i++) {
                letterSet.add(txt.substring(i, j++));
            }
            textField.clear();
        });

        x.setOnAction(e -> {
            String text = textField.getText();
            letterSet.add(text.substring(text.length() - 1));
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
        });
        textField.setEditable(false);
        seeWords.setPrefHeight(200);
        seeWords.setPrefWidth(150);
    }

    /**
     * Reset all configurations.
     */
    private void reset() {
        this.shuffleDices();
        this.randomizeDices();
        this.myWords.clear();
        this.letterSet.clear();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.setUp(primaryStage);
        this.initializeDices();
        this.shuffleDices();
        this.randomizeDices();
        this.setActions();
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
     * Generate the random letters for the dices and add them to a set.
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
                letterSet.add(randomized[get]);
                buttons[i][j].setText(randomized[get++]);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean verifyWord(String txt) {
        return session.verifyWord(txt);
    }
}
