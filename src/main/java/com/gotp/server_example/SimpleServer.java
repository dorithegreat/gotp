package com.gotp.server_example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.gotp.game_mechanics.board.move.MovePlace;

/**
 * A simple server that receives an object from a client and sends a response object back.
 */
public final class SimpleServer {

    /**
     * Private constructor to prevent instantiation.
     */
    private SimpleServer() { }

    /**
     * The main method.
     * @param args The command line arguments.
     */
    public static void main(final String[] args) {
        final int portNumber = 12345;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle the client in a new thread
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(final Socket clientSocket) {
        try {
            ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());

            // Receive an object from the client
            MovePlace receivedObject = (MovePlace) objectInput.readObject();
            System.out.println("Received from client: " + receivedObject.getField());

            // Example response to the client
            SimpleMessage responseObject = new SimpleMessage(
                "Server received: " + receivedObject.getPieceType().longName()
            );
            objectOutput.writeObject(responseObject);

            // Close the connection when done
            clientSocket.close();
            objectInput.close();
            objectOutput.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
