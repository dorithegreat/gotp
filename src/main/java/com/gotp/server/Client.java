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
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageDebug;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.game_thread_messages.MessageGameStarted;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromClient;
import com.gotp.server.messages.game_thread_messages.MessageMoveFromServer;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVPSuccess;

/**
 * Client.
 */
public final class Client extends Application {

    public enum GameType {
        PVP, BOT
    }

    /**
     * communicator with the GUI part of the board
     */
    private BoardCommunicator board;

    /**
     * layer of abstraction before server that passes moves to the actual server
     */
    private Communicator server;

    /**
     * basically an ID of this player
     */
    private int authenticationKey;

    // * Filip put this here "to calm down pmd" and I'm not sure what for but I have no reason to delete it
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
        board.setPlayer(PieceType.BLACK);
        
        try (Socket socket = new Socket(serverAddress, serverPort)) {
            System.out.println("Connected to server.");
            server = new Communicator(socket); //sends communication with the server through a specialized class


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
        //all messages will be replaced with actual message types
        //probably I will also replace the string with an enum

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

    //------------------------------------------------------
    //          functions for sending info to server
    //------------------------------------------------------

    /**
     * communicates player's last move to the server
     * it has already been checked if it's legal
     * if it's returned as invalid something went deeply wrong
     */
    public void sendMove(Move move) throws IOException, ClassNotFoundException {
        server.send(new MessageMoveFromClient(authenticationKey, move));

        //check server's response here
        //currently the server doesn't send any response in return so there's nothing to test

        Message response = server.receive();
        // *needs to be changed to MOVE_FROM_SERVER but that type of message doesn't yet exist
        if (response.getFunction() == MessageFunction.DATA_MESSAGE && response.getType() == MessageType.MOVE_FROM_CLIENT) {
            Move serverMove = ((MessageMoveFromServer) response).getMove();   
        }
    }

    /**
     * sends a message to the server that the user wants to pass
     */
    public void sendPass() throws IOException, ClassNotFoundException{
        //server.send(/*pass message */);
        Message response = server.receive();
        //process the response
    }

    /**
     * sends a message that the user wants to immediately lose the game and disconnect
     */
    public void sendResign() throws IOException, ClassNotFoundException{
        //server.send(/*resign message */);
        Message response = server.receive();
        //process the response
    }

    public void requestGameMode(GameType mode, int size) throws IOException, ClassNotFoundException{
        switch (mode) {
            case PVP:
                server.send(new MessageGameRequestPVP(size));
                break;
            case BOT:
                //server.send( bot request);
            default:
                break;
        }
        Message response = server.receive();
        if (response.getFunction() == MessageFunction.RESPONSE && response.getType() == MessageType.GAME_REQUEST_SUCCESS) {
            System.out.println("game request approved");
            response = server.receive();
            if (response.getFunction() == MessageFunction.RESPONSE && response.getType() == MessageType.GAME_STARTED) {
                //ugly but will stay for now
                authenticationKey = ((MessageGameStarted) response).getAuthenticationKey();
                board.setPlayer(((MessageGameStarted) response).getPlayerPieceType());
            }
            //server registered game request as valid but returned something else than Game Started
            System.out.println("Game could not be started");
        }
        else{
            System.out.println("game request denied");
            return;
        }
    }

    public void requestDatabase(){
        //send an appropriate message
        //get a response and call method in BoardCommunicator
    }
}
