/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sdcom.view;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
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

        TilePane tile = new TilePane(1, 2);

        tile.setPrefColumns(3);

        setProducts(tile);

        border.setCenter(tile);

        stage.setScene(new Scene(border, 800, 600));
    }

    private void setProducts(TilePane t) {        
        Iterator it = products.values().iterator();

        Image image = new Image(getClass().getResourceAsStream("store.png"));

        while (it.hasNext()) {
            Product product = (Product) it.next();
            Button b = new Button(product.getName(), new ImageView(image));
            t.getChildren().add(b);
        }
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
        StoreScreen s = new StoreScreen();
        s.loadProducts();
        launch(args);
    }
}
