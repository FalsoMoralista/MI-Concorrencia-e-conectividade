/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.game;

import br.ecomp.uefs.model.Ranking;
import br.ecomp.uefs.multicast.CommunicationGroup;
import br.ecomp.uefs.multicast.MultiPackage;
import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import javafx.application.Application;
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
import br.ecomp.uefs.model.User;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * The main game screen
 *
 * @author Luciano Araujo Dourado Filho
 */
public class InGameScreen extends Application implements Serializable {

    private User player;

    Game game;

    private Stage stage;

    private HashMap<String, Integer> dictionary;

    protected Button[][] buttons = new Button[4][4];
    private TextField textField = new TextField();
    private Button submit = new Button("submit");
    private Button clear = new Button("clear");
    private Button x = new Button("x");
    private LinkedList<String> myWords = new LinkedList<>();
    private LinkedList<String> letterSet;
    private ListView seeWords = new ListView();
    private ListView seePlayers = new ListView();

    public InGameScreen() {

    }

    public InGameScreen(Game game, User player) throws IOException {

        this.game = game;
        this.letterSet = game.getLetterSet();
        this.player = player;

        Iterator it = game.users.entrySet().iterator();
        CommunicationGroup group = player.getGroup();
        List<String> users = new LinkedList<>();
        while (it.hasNext()) {
            Entry e = (Entry) it.next();
            User u = (User) e.getValue();
            group.addParticipant(u);
            if (u.toString().equals(player.toString())) {
                users.add(u.toString() + " (me)");
            } else {
                users.add(u.toString());
            }
        }
        seePlayers.setItems(new ObservableListWrapper(users));
    }

    private void loadDictionary() throws IOException {

        this.dictionary = new HashMap<>();

        Path path = Paths.get(new File("/home/luciano/Desktop/LUCIANO-UEFS/Java/MI - Concorrência e Conectividade/Boggle/src/br/ecomp/uefs/game/resources/English (American).dic"/*"src/br/ecomp/uefs/game/resources/English (American).dic"*/).getPath());

        Stream<String> lines = Files.lines(path);
        int[] i = new int[1];
        lines.forEach(line -> {
            dictionary.put(line.toLowerCase(), i[0]++);
        });
    }

    /**
     * Set up screen properties.
     */
    private void setUp(Stage primaryStage) {

        primaryStage.setTitle("Boggle");

        BorderPane border = new BorderPane();

        // top 
        GridPane top = new GridPane();
        top.setAlignment(Pos.CENTER);
        top.setHgap(10);
        top.setVgap(10);
        top.autosize();
        top.setPadding(new Insets(25, 50, 0, 0));

        border.topProperty().setValue(top);
        //---------------------------------------------------------------------
        // MIDDLE        
        GridPane mid = new GridPane();

        mid.setAlignment(Pos.CENTER);
        mid.setHgap(0);
        mid.setVgap(10);
        mid.autosize();
        mid.setPadding(new Insets(50, 0, 0, 0));

        border.setCenter(mid);
        //---------------------------------------------------------------------
        // Right
        GridPane right = new GridPane();
        right.setAlignment(Pos.CENTER);
        right.setHgap(0);
        right.setVgap(10);
        right.autosize();
        right.setPadding(new Insets(0, 50, 0, 0));

        right.add(new Label("         My words"), 0, 0);

        right.add(seeWords, 0, 1);
        seeWords.setPrefHeight(300);
        seeWords.setPrefWidth(150);

        border.setRight(right);
        //---------------------------------------------------------------------
        // left
        GridPane left = new GridPane();
        left.setAlignment(Pos.CENTER);
        left.setHgap(0);
        left.setVgap(10);
        left.autosize();
        left.setPadding(new Insets(0, 0, 0, 50));

        left.add(new Label("          Players"), 0, 0);

        left.add(seePlayers, 0, 1);
        seePlayers.setPrefHeight(300);
        seePlayers.setPrefWidth(150);

        border.setLeft(left);
        //---------------------------------------------------------------------        
        //Bottom
        GridPane bot = new GridPane();
        bot.setAlignment(Pos.CENTER);
        bot.setHgap(10);
        bot.setVgap(5);
        bot.autosize();
        bot.setPadding(new Insets(0, 0, 80, 30));

        bot.add(x, 0, 1);

        bot.add(textField, 1, 1);

        bot.add(submit, 2, 1);

        bot.add(clear, 3, 1);

        textField.setPrefHeight(25);

        border.setBottom(bot);
        //---------------------------------------------------------------------
        Scene scene = new Scene(border, 800, 600);
        primaryStage.setScene(scene);
    }

    /**
     * Initialize all dices.
     */
    public void initializeButtons() {
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

                if (txt.length() >= 2) {

                    if (!myWords.contains(txt)) {
                        if (verifyWord(txt)) {
                            myWords.add(txt);
                            seeWords.setItems(new ObservableListWrapper(myWords));
                            int wordIndex = myWords.indexOf(txt);
                            MultiPackage pack = new MultiPackage(player.toString(), "w", new Word(txt, wordIndex));
                            try {
                                player.getGroup().addUserMessage(player.toString(), player.toString(), new Word(txt, wordIndex));
                                player.multicast(pack);
                            } catch (IOException ex) {
                                Logger.getLogger(InGameScreen.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } else {
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

    }

    /**
     * Reset all configurations.
     */
    private void reset() {
        game.reset();
        this.myWords.clear();
        this.setDices();
    }

    private void setDices() {
        int get = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j].setText(letterSet.get(get++));
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = stage;
        this.setUp(primaryStage);
        this.initializeButtons();
        this.setDices();
        this.setActions();
        loadDictionary();

        BorderPane border = (BorderPane) primaryStage.getScene().getRoot();
        GridPane grid = (GridPane) border.getCenter();

        TilePane pane = new TilePane();
        pane.setPrefColumns(4);
        pane.setPrefRows(4);
        pane.setVgap(10);
        pane.setHgap(10);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Button b = buttons[i][j];
                b.setPrefSize(50, 50);
                pane.getChildren().add(b);
            }
        }

        grid.add(pane, 0, 0);
        countTime();
        primaryStage.show();
    }

    private void countTime() {

        Runnable r = () -> {
            long start = game.getSTARTING_TIME();
            while (((System.currentTimeMillis() - start) / 1000) != 60) { // 180
            }
            Platform.runLater(() -> {
                endGame();
            });
        };
        new Thread(r).start();
    }

    private void endGame() {
        LinkedList<Ranking> rank = player.getGroup().account();

        LinkedList<String> messages = new LinkedList<>();

        messages.add("Ranking:"+"\n");
        
        int count = 1;
        for(int i = rank.size(); i <= 0; i--){
            messages.add(count+"-"+""+rank.get(i)+"\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Winner"+"\n"+rank.get(rank.size()-1));
        alert.show();
    }

    private boolean verifyWord(String txt) {
        return dictionary.containsKey(txt.toLowerCase());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
