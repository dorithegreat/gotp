package com.gotp.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.gotp.GUIcontrollers.BoardController;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;

/**
 * Client.
 */
public final class Client extends Application {

    /**
     * stores all data about current state of the game. 
     * also processes moves.
     */
    private GameState state;

    /**
     * communicator with the GUI part of the board
     */
    private BoardCommunicator board;

    /** Private constructor. Disallow instantiation. */
    public Client() { }

    /**
     * Main method.
     */
    public void start(Stage stage) throws IOException {
        String serverAddress = "localhost";
        Scanner scanner = new Scanner(System.in);
        final int serverPort = 12345;

        Scene scene = new Scene(loadFXML("testScene"));
        stage.setScene(scene);
        stage.show();

        board = BoardCommunicator.getInstance();
        board.setClient(this);
        try { //inside try-catch because it most likely doesn't yet exist
            board.getBoard().doSomething();
        } catch (Exception e) {
            System.out.println("not yet initialized");
        }
        
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            Communicator server = new Communicator(socket); //sends communication with the server through a specialized class

            String input;
            Message response;

            //I will most likely delete this as the cient only needs to send and receive if something happens
            //and it can go directly to communicator without the need for this loop
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
            System.out.println("can't communicate with the server");
        }
    }

    /**
     * extracted code for loading the inital scene.
     * could technically be used for loading any other scene though I don't think I'll need it
     * @param fxml
     * @return
     * @throws IOException
     */
    private static Parent loadFXML(final String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Client.class.getResource(fxml + ".fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("testScene.fxml"));
        return fxmlLoader.load();
    }
    
    /**
     * should send a message to the server through a Communicator
     * @param message currently a string, will probably be changed to an enum later on
     */
    public void sendToServer(String message){
        System.out.println("would've sent a message to the server: " + message);
    }

    /**
     * main method launching javafx.
     * @param args standard arguments
     */
    public static void main(final String[] args) {
        System.out.println("started");
        launch();
    }
}
