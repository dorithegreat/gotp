package com.gotp.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gotp.game_mechanics.board.GameHistory;

public class TextDatabase implements Database {
    private static String source = "text_base.txt";

    /**
     * Insert a game history into the database.
     * @param gameHistory
     */
    @Override
    public void insertGameHistory(final GameHistory gameHistory) {
        String toInsert = GameHistoryCoder.historyToString(gameHistory);
        appendToFile(source, toInsert);
    }

    /**
     * Get a game history from the database.
     * @return GameHistory
     */
    @Override
    public GameHistory getGameHistory(final int id) {
        String toDecode = parseFile(source).get(id);
        return GameHistoryCoder.stringToHistory(toDecode);
    }

    /**
     * Get all Game Histories from the database.
     * @return List<GameHistory>
     */
    @Override
    public List<GameHistory> getAllGameHistories() {
        return new ArrayList<GameHistory>();
    }

    /**
     * Get the last game history from the database.
     * @return GameHistory
     */
    @Override
    public GameHistory getLastGameHistory() {
        Map<Integer, String> everyGame = parseFile(source);
        String toDecode = everyGame.get(everyGame.size() - 1);
        return GameHistoryCoder.stringToHistory(toDecode);
        // return GameHistoryCoder.stringToHistory(everyGame.get(2));
    }

    /**
     * Return key value pairs.
     * @param filename
     * @return Map
     */
    public static Map<Integer, String> parseFile(final String filename) {
        Map<Integer, String> resultMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line into number and string parts
                String[] parts = line.split(" ", 2);
                if (parts.length == 2) {
                    int number = Integer.parseInt(parts[0]);
                    String text = parts[1].trim(); // Remove leading and trailing spaces
                    resultMap.put(number, text);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultMap;
    }

    /**
     * Append to file.
     * @param filename
     * @param content
     */
    private static void appendToFile(final String filename, final String content) {
        Map<Integer, String> allGames = parseFile("./text_base.txt");
        int newIndex = allGames.size();


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(newIndex + " " + content);
            writer.newLine(); // Add a newline after appending the content
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Database otherDB = new DatabaseMock();
        // GameHistory encoded = GameHistoryCoder.stringToHistory(parseFile(source).get(2));
        // System.out.println(encoded);
        
        // Database thisDB = new TextDatabase();
        // thisDB.getLastGameHistory();
    }
}
