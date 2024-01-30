package com.gotp.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.gotp.game_mechanics.board.GameHistory;

public class SQLiteDatabase implements Database {
    /**
     * Serialize GameHistory object to bytes[].
     * @param gameHistory
     * @return byte[]
     */
    private byte[] serializeGameHistory(final GameHistory gameHistory) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(gameHistory);
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * Encode serialized data to Base64 string.
     * @param serializedData
     * @return String
     */
    private String encodeSerialized(final byte[] serializedData) {
        return Base64.getEncoder().encodeToString(serializedData);
    }

    /**
     * Decode Base64 string to bytes[].
     * @param encodedData
     * @return byte[]
     */
    private byte[] decodeSerialized(final String encodedData) {
        return Base64.getDecoder().decode(encodedData);
    }

    /**
     * Deserialize bytes[] to GameHistory object.
     * @param serializedData
     * @return GameHistory
     */
    private GameHistory deserializeGameHistory(final byte[] serializedData) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (GameHistory) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Insert a game history into the database.
     * @param gameHistory
     */
    @Override
    public void insertGameHistory(final GameHistory gameHistory) {
        byte[] serializedGame =  serializeGameHistory(gameHistory);
        String encodedGame = encodeSerialized(serializedGame);

        Connection c = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:game_history_database.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            String sql = "INSERT INTO games (history_object) " + "VALUES (?);";
            preparedStatement = c.prepareStatement(sql);
            preparedStatement.setString(1, encodedGame);
            preparedStatement.executeUpdate();

            c.commit();
            c.close();
            preparedStatement.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");
    }

    /**
     * Get a game history from the database.
     * @return GameHistory
     */
    @Override
    public GameHistory getGameHistory(final int id) {
        Connection dbConnection = null;
        PreparedStatement statement = null;

        try {
            // Connection
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:game_history_database.sqlite");
            dbConnection.setAutoCommit(false);

            // Statement
            String selectQuery =  "SELECT * FROM games WHERE id = ?;";
            statement = dbConnection.prepareStatement(selectQuery);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            GameHistory gameHistory = null;
            if (result.next()) {
                String encodedGame = result.getString("history_object");
                byte[] decodedGame = decodeSerialized(encodedGame);
                gameHistory = deserializeGameHistory(decodedGame);
            }

            result.close();
            statement.close();
            dbConnection.close();

            return gameHistory;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        // System.out.println("Operation done successfully");
        return null;
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
        Connection dbConnection = null;
        Statement statement = null;

        try {
            // Connection
            Class.forName("org.sqlite.JDBC");
            dbConnection = DriverManager.getConnection("jdbc:sqlite:game_history_database.sqlite");
            dbConnection.setAutoCommit(false);

            // Statement
            String selectQuery =  "SELECT * FROM games ORDER BY id DESC LIMIT 1;";
            statement = dbConnection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);

            GameHistory gameHistory = null;
            if (result.next()) {
                String encodedGame = result.getString("history_object");
                byte[] decodedGame = decodeSerialized(encodedGame);
                gameHistory = deserializeGameHistory(decodedGame);
            }

            result.close();
            statement.close();
            dbConnection.close();

            return gameHistory;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        // System.out.println("Operation done successfully");
        return null;
    }
}
