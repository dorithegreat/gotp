package com.gotp.server_example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

/**
 * Simple client that sends a message to the server and receives a response.
 */
public final class SimpleClient {

    /** Private constructor. Disallow instantiation. */
    private SimpleClient() { }

    /**
     * Main method.
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        String serverAddress = "localhost";
        final int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");

            // Create an object to send to the server
            SimpleMessage myObject = new SimpleMessage("Hello from client!");
            final Vector field = new Vector(6, 9);
            Move move = new MovePlace(field, PieceType.BLACK);

            // Send the object to the server
            ObjectOutputStream objectOutput = new ObjectOutputStream(socket.getOutputStream());
            objectOutput.writeObject(move);

            // Receive a response object from the server
            ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream());
            SimpleMessage response = (SimpleMessage) objectInput.readObject();
            System.out.println("Server response: " + response.getMessage());

            // Close the streams
            objectOutput.close();
            objectInput.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
