/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ecomp.uefs.view;

import br.ecomp.uefs.controller.Controller;
import com.sun.javafx.collections.ObservableListWrapper;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import shared.exception.InvalidTypeOfRequestException;
import shared.exception.MaxAmountOfPlayersReachedException;
import shared.exception.NoneLogInException;
import shared.exception.UserAlreadyBindedException;
import shared.model.Lobby;
import br.ecomp.uefs.model.User;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class MainMenu extends Application {

    private Controller controller = new Controller("localhost");
    private ListView availableRooms = new ListView();
    
    private Button join = new Button("Join Lobby");
    
    private Stage stage;
    
    public MainMenu() {

    }

    public MainMenu(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        this.before(primaryStage);

        if(this.controller.getInstance() instanceof User){
            setListItems();
            synchronize();
        }

        primaryStage.show();
    }

    private void before(Stage primaryStage) {
        primaryStage.setTitle("Main menu");
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(10);
        grid.autosize();
        
        this.setProperties();
        
        grid.add(availableRooms, 0, 1);
        BorderPane border = new BorderPane(grid);
        
        GridPane bottomGrid = new GridPane();
        
        bottomGrid.setAlignment(Pos.TOP_CENTER);
        bottomGrid.setHgap(10);
        bottomGrid.setVgap(0);
        bottomGrid.autosize();
        bottomGrid.add(join, 0, 0);
        
        border.setBottom(bottomGrid);
        Scene scene = new Scene(border,500,300);

        primaryStage.setScene(scene);
    }
    
    private void setProperties(){

        availableRooms.setPrefHeight(200);
        availableRooms.setPrefWidth(300);

        join.setOnAction(e ->{
            Alert alert = null;
            try {

                Lobby lobby = ((Lobby)availableRooms.getSelectionModel().getSelectedItem());
                lobby = controller.enterLobby(lobby.getId());

                WaitingLobby waitingLobby = new WaitingLobby(lobby, controller);
                waitingLobby.start(stage);
            } catch (InvalidTypeOfRequestException | IOException | ClassNotFoundException | UserAlreadyBindedException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MaxAmountOfPlayersReachedException | NoneLogInException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(ex.getMessage());
                alert.show();
            } catch (Exception ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
            
    private void setListItems() throws IOException, UnknownHostException, ClassNotFoundException, InvalidTypeOfRequestException{
        this.availableRooms.setItems(new ObservableListWrapper(controller.getAvailableRooms()));
    }
        
    private void synchronize(){
        Runnable r = ( ) -> {            
            try {
                sleep(5000);
                setListItems();
            } catch (IOException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidTypeOfRequestException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
        };
        new Thread(r).start();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}
