/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.inova.view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import java.util.Scanner;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class Main {

    private static Scanner scanner;
    private final Properties NEWS_LIST;

    public Main() throws FileNotFoundException, IOException {
        NEWS_LIST = new Properties();
        NEWS_LIST.load(new FileInputStream(new File("db/news_list.properties")));
    }

    /**
     * Show the menu options.
     * @throws java.io.IOException
     */
    public void menu() throws IOException {
        scanner = new Scanner(System.in);
        boolean op = true;
        while (op) {
            System.out.println("Menu");
            System.out.println("------------------------------");
            System.out.println("| 1 - Rate a news | 0 - exit |");
            System.out.println("------------------------------");
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    System.out.println("Choose one of the options below:");
                    listNews();
                    int newsID = Integer.parseInt(scanner.nextLine());
                    System.out.println("inform the rating:");
                    int rate = Integer.parseInt(scanner.nextLine());
                    rateNews(newsID, rate);
                    break;
                case 0:
                    op = false;
                    break;
            }
        }
        System.exit(0);
    }

    /**
     * Exhibit all the available news to rate.
     */
    public void listNews() {
        for (int i = 0; i < NEWS_LIST.size(); i++) {
            System.out.println("--------------");
            System.out.println("| " + i + "-" + NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(i) + ']') + " |");
        }
        System.out.println("--------------");
    }

    /**
     * Rate a news.
     *
     * @param newsID
     * @param rate
     * @throws java.io.IOException
     */
    public void rateNews(int newsID, int rate) throws IOException {
        System.out.println("Rating " + rate + " to the news " + newsID);

        String name = NEWS_LIST.getProperty("NEWS_NAME" + '[' + Integer.toString(newsID) + ']');

        File db = new File("db/news/" + name + ".txt");

        if (!db.exists()) {
            db.createNewFile();
        }

        Path path = Paths.get(db.getPath());

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
            writer.write(String.valueOf(rate));
            writer.newLine();
        }
        System.out.println("Sucessfully");
    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.menu();
    }
}
