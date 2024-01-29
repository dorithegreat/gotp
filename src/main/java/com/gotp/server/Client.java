package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javafx.stage.Stage;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;

/**
 * Client.
 */
public final class Client {

    /** Private constructor. Disallow instantiation. */
    private Client() { }

    /**
     * Main method.
     * @param args command line arguments
     */
    public void start(Stage stage) throws IOException {
        String serverAddress = "localhost";
        Scanner scanner = new Scanner(System.in);
        final int serverPort = 12345;

        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            Communicator server = new Communicator(socket); //sends communication with the server through a specialized class

            String input;
            Message response;

            while (true) {
                // Send a message to the server
                System.out.print("[->] ");
                input = scanner.nextLine();
                server.send(new MessageDebug(input));

                if ("exit".equals(input)) {
                    break;
                }

                // Receive a message from the server
                response = server.receive();
                if (response instanceof MessageDebug) {
                    System.out.println("[<-] " + ((MessageDebug) response).getDebugMessage());
                }

            }

            // Close the connection
            server.close();
            scanner.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}