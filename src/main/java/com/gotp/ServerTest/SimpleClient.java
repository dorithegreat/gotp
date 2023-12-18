package com.gotp.ServerTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {

    public static void main(String[] args) {
        final String serverAddress = "localhost";
        final int portNumber = 12345;

        try (Socket socket = new Socket(serverAddress, portNumber);
             BufferedReader serverInputReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter outputWriter = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server. Type a message:");

            // Read messages from the user and send them to the server
            String userInput;
            while ((userInput = userInputReader.readLine()) != null) {
                outputWriter.println(userInput);

                // Receive and display the server's response
                String serverResponse = serverInputReader.readLine();
                System.out.println("Server says: " + serverResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
