/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import br.ecomp.uefs.controller.Controller;
import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import shared.exception.InsufficientAmountOfPlayersException;
import shared.exception.InvalidTypeOfRequestException;
import shared.model.Game;
import shared.model.GameSession;
import shared.model.Lobby;
import shared.model.User;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class WaitingLobby extends Application {

    private Lobby lobby;
    private Controller controller;

    private ListView seePlayers;
    private Button start = new Button("Start game");
    
    private boolean synchronizing;

    private Stage stage;
    
    public WaitingLobby(Lobby lobby, Controller controller) {
        this.lobby = lobby;
        this.controller = controller;
        this.seePlayers = new ListView();
        synchronizing = true;
    }

    public WaitingLobby() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        before(primaryStage);
        syncrhonize();
    }

    private void syncrhonize() {
        Runnable r = () -> {
            while (synchronizing) {
                try {
                    Thread.sleep(5000); // waits for 15 seconds then synchronize with the server
                    LinkedList<Lobby> lobbies = controller.getAvailableRooms();
                    Lobby l = lobbies.get(this.lobby.getId());
                    this.seePlayers.setItems(new ObservableListWrapper(listPlayers()));
                    
                } catch (IOException | ClassNotFoundException | InvalidTypeOfRequestException | InterruptedException ex) {
                    Logger.getLogger(WaitingLobby.class.getName()).log(Level.SEVERE, null, ex);
                }catch(NullPointerException ex){
                    
                }
            }
        };
        new Thread(r).start();
    }

    private void before(Stage primaryStage) {

        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(10);
        grid.autosize();

        grid.add(seePlayers, 0, 2);

        setProperties();

        BorderPane border = new BorderPane(grid);

        GridPane bottomGrid = new GridPane();

        bottomGrid.setAlignment(Pos.TOP_CENTER);
        bottomGrid.setHgap(10);
        bottomGrid.setVgap(0);
        bottomGrid.autosize();

        if (controller.getInstance().toString().equals(lobby.getLeader().toString())) {
            bottomGrid.add(start, 0, 0);
        }

        border.setBottom(bottomGrid);
        Scene scene = new Scene(border, 500, 300);

        primaryStage.setScene(scene);
    }

    private void setProperties() {

        start.setOnAction(e -> {
            try {                
                Game game = controller.startGame(lobby.getId());
                synchronizing = false;
                new InGameScreen(game).start(stage);
            } catch (IOException ex) {
                Logger.getLogger(WaitingLobby.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(WaitingLobby.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InsufficientAmountOfPlayersException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ex.getMessage());
                alert.show();
                Logger.getLogger(WaitingLobby.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(WaitingLobby.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        this.seePlayers.setPrefHeight(250);
        this.seePlayers.setPrefWidth(200);
        this.seePlayers.setItems(new ObservableListWrapper(listPlayers()));
    }

    private LinkedList<String> listPlayers() {

        LinkedList<String> players = new LinkedList<>();

        HashMap<String, User> lobbyPlayers = lobby.getPlayers();

        lobbyPlayers.keySet().forEach(s -> {

            if (lobby.getLeader().toString().equals(s)) {
                players.add(s + " - Group leader");
            } else {
                players.add(s);
            }
        });

        return players;

    }
}