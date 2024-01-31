package com.gotp.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import com.gotp.game_mechanics.board.GameHistory;

public final class GameHistoryCoder {

    private GameHistoryCoder() { }

     /**
     * Serialize GameHistory object to bytes[].
     * @param gameHistory
     * @return byte[]
     */
    public static byte[] serializeGameHistory(final GameHistory gameHistory) {
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
    public static String encodeSerialized(final byte[] serializedData) {
        return Base64.getEncoder().encodeToString(serializedData);
    }

    /**
     * Decode Base64 string to bytes[].
     * @param encodedData
     * @return byte[]
     */
    public static byte[] decodeSerialized(final String encodedData) {
        return Base64.getDecoder().decode(encodedData);
    }

    /**
     * Deserialize bytes[] to GameHistory object.
     * @param serializedData
     * @return GameHistory
     */
    public static GameHistory deserializeGameHistory(final byte[] serializedData) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(serializedData);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (GameHistory) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Convert from gameHistory to String.
     * @param gameHistory
     * @return String
     */
    public static String historyToString(final GameHistory gameHistory) {
        return encodeSerialized(serializeGameHistory(gameHistory));
    }

    /**
     * Convert from String to GameHistory.
     * @param encoded
     * @return GameHistory.
     */
    public static GameHistory stringToHistory(final String encoded) {
        return deserializeGameHistory(decodeSerialized(encoded));
    }
}
