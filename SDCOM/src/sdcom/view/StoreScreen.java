/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.view;

import com.sun.javafx.collections.ObservableListWrapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sdcom.model.Client;
import sdcom.model.Product;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class StoreScreen extends Application {

    private HashMap<Integer, Product> products;
    private ListView<Product> productView;
    private Button select;
    private Button buy;
    private Product cart;
    private Label selected = new Label("Selected: ");
    private Text onCart = new Text();
    private Client op;
    private String db;

    public StoreScreen() {

    }

    public StoreScreen(String name) throws IOException, FileNotFoundException, RemoteException, NotBoundException {
        products = new HashMap<>();
        op = new Client(name);
        db = "db/" + name + ".db";
    }

    /**
     *  Load the list of products from de database. 
     */
    public void loadProducts() {

        try {

            List<String> lines = Files.readAllLines(Paths.get(db));

            lines.forEach(line -> {

                StringTokenizer token = new StringTokenizer(line, "|");

                while (token.hasMoreElements()) {
                    String id = token.nextToken();
                    String name = token.nextToken();
                    String quantity = token.nextToken();
                    String price = token.nextToken();
                    Product p = new Product(name, Integer.parseInt(id), Integer.parseInt(quantity), Double.parseDouble(price));
                    products.put(p.getID(), p);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Starts the screen properties. 
     */
    private void screenProperties(Stage stage) {

        BorderPane border = new BorderPane();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(0);
        grid.autosize();

        productView = new ListView();

        setProducts(productView);

        productView.setPrefHeight(200);
        productView.setPrefWidth(500);

        grid.add(productView, 0, 0);

        border.setCenter(grid);

        GridPane bottomGrid = new GridPane();
        bottomGrid.setAlignment(Pos.CENTER);
        bottomGrid.setAlignment(Pos.CENTER);
        bottomGrid.setHgap(10);
        bottomGrid.setVgap(15);
        bottomGrid.autosize();
        bottomGrid.setPadding(new Insets(0, 0, 100, 0));

        buy = new Button("Buy");
        select = new Button("Select");

        selected.setFont(javafx.scene.text.Font.font("", FontWeight.NORMAL, 20));

        bottomGrid.add(selected, 0, 0);
        bottomGrid.add(onCart, 1, 0);

        bottomGrid.add(select, 0, 3);
        bottomGrid.add(buy, 1, 3);

        border.setBottom(bottomGrid);

        stage.setScene(new Scene(border, 800, 600));
    }

    /**
     *  Set the products list on the screen. 
     */
    private void setProducts(ListView<Product> l) {

        LinkedList<Product> listProducts = new LinkedList<>();

        Iterator it = products.values().iterator();

        while (it.hasNext()) {
            Product product = (Product) it.next();
            listProducts.add(product);
        }

        ObservableListWrapper<Product> olw = new ObservableListWrapper<>(listProducts);

        l.getItems().addAll(listProducts);
    }
    
    /**
     * Set all buttons actions. 
     */
    private void setActions() {
        
        select.setOnAction(e -> {
            cart = productView.getSelectionModel().getSelectedItem();
//            if(cart.isLocked()){
//                System.out.println("");
//            }
            cart.lock();
            onCart.setText(cart.getName());
        });

        buy.setOnAction(e -> {
            Alert alert;
            try {
                boolean canSell = op.sell(cart);

                if (canSell) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Sold successfully");
                    alert.show();
                    loadProducts();
                    setProducts(productView);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Sorry, you almost got it");
                    alert.show();
                }
            } catch (RemoteException ex) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(ex.getMessage());
                alert.show();
            }            
        });
    }

    /**
     * Starts the application. 
     * @param stage
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        loadProducts();
        screenProperties(stage);
        setActions();
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
