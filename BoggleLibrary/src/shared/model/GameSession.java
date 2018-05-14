/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shared.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 *
 * @author Luciano Araujo Dourado Filho
 */
public class GameSession {
    private HashMap<String,Integer> dictionary;
    private User user;
    private LinkedList<User> participants;
    
    private void loadDictionary() throws IOException{
        this.dictionary = new HashMap<>();
        
        Path path = Paths.get(new File("src/br/ecomp/uefs/resources/English (American).dic").getPath());

        Stream<String> lines = Files.lines(path);
        int[] i = new int[1];
        lines.forEach(line -> {
            dictionary.put(line, i[0]++);            
        });
    }
    
    public GameSession() throws IOException{
        loadDictionary();
    }
    
    public boolean verifyWord(String word){
        try{
            dictionary.get(word).toString();
            return true;
        }catch(NullPointerException ex){
            return false;
        }
    }
}
