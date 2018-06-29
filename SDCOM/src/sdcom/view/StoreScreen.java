/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.view;

import com.sun.javafx.collections.ObservableListWrapper;
import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import sdcom.model.Product;

/**
 *
 * @author luciano
 */
public class StoreScreen extends Application {

    private HashMap<Integer, Product> products;

    public StoreScreen() {
        products = new HashMap<>();
    }

    public void loadProducts() {

        String db = "src/sdcom/db/database.db";

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
            System.out.println(products.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void screenProperties(Stage stage) {

        BorderPane border = new BorderPane();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(0);
        grid.autosize();

        ListView list = new ListView();

        setProducts(list);

        list.setPrefHeight(200);
        list.setPrefWidth(500);

        grid.add(list, 0, 0);

        border.setCenter(grid);

        stage.setScene(new Scene(border, 800, 600));
    }

    private void setProducts(ListView l) {

        LinkedList<Product> listProducts = new LinkedList<>();

        Iterator it = products.values().iterator();

        Image image = new Image(getClass().getResourceAsStream("store.png"));

        while (it.hasNext()) {
            Product product = (Product) it.next();
            listProducts.add(product);
        }
        ObservableListWrapper<Product> olw = new ObservableListWrapper<>(listProducts);
        l.getItems().addAll(listProducts);
    }

    public synchronized void buy(Product p) {

    }

    @Override
    public void start(Stage stage) throws Exception {
        loadProducts();
        screenProperties(stage);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
