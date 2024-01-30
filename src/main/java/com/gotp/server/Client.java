package com.gotp.server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.database_messages.MessageDatabaseRequest;
import com.gotp.server.messages.database_messages.MessageDatabaseResponse;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.game_thread_messages.MessageGameOver;
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
     * basically an ID of this player
     */
    private int authenticationKey;

    private BlockingQueue<Message> serverQueue;

    /**
     * I made this public so that the BoardCommunicator could access it directly.
     * * I really should at least make a getter instead
     * it doesn't matter that much because it's only an output anyway
     */
    public BlockingQueue<Message> receivedQueue;


    // * Filip put this here "to calm down pmd" and I'm not sure what for but I have no reason to delete it
    /** Private constructor. Disallow instantiation. */
    public Client() { }

    /**
     * Main method.
     */
    public void start(Stage stage) throws IOException, InterruptedException {

        Scene scene = new Scene(loadFXML("testScene"));
        stage.setScene(scene);
        stage.show();

        //links to the BoardCommunicator which handles the GUI
        board = BoardCommunicator.getInstance();
        board.setClient(this);
        
        //creates a separate thread that automatically communicates with the servers
        serverQueue = new LinkedBlockingDeque<>();
        receivedQueue = new LinkedBlockingDeque<>();
        ClientThread clientThread = new ClientThread(serverQueue, receivedQueue);
        new Thread(clientThread).start();

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
     * main method launching javafx.
     * @param args standard arguments
     */
    public static void main(final String[] args) {
        launch();
    }

    //------------------------------------------------------
    //          functions for sending info to server
    //------------------------------------------------------

    /**
     * 
     */
    public void checkInbox() throws InterruptedException, IOException{
            Message response = receivedQueue.take();
            System.out.println("inbox:");
            System.out.println(response.getType());
            if (response.getType() == MessageType.GAME_STARTED) {
                startGame(response);
            }
            else if (response.getType() == MessageType.MOVE_FROM_SERVER) {
                processIncomingMove(response);
            }
            else if (response.getType() == MessageType.DATABASE_RESPONSE) {
                startReplay(response);
            }
            else if (response.getType() == MessageType.GAME_OVER) {
                System.out.println("game over");
                board.endGame((MessageGameOver) response);
            }
    }

    /**
     * initializes the game once the server connects this client with an opponent
     */
    public void startGame(Message response) throws IOException, InterruptedException{
        System.out.println("game started");

        MessageGameStarted gameStarted = (MessageGameStarted) response;
        authenticationKey = gameStarted.getAuthenticationKey();
        board.setPlayer(gameStarted.getPlayerPieceType());
        if (gameStarted.getPlayerPieceType() == PieceType.WHITE) {
            checkInbox();
        }
    }

    public void processIncomingMove(Message response) throws IOException{
        MessageMoveFromServer moveMessage = (MessageMoveFromServer) response;
        board.makeMove(moveMessage.getMove());
    }

    public void startReplay(Message response) throws IOException, InterruptedException {
        MessageDatabaseResponse dataMessage = (MessageDatabaseResponse) response;
        DatabaseProcessor databaseProcessor = new DatabaseProcessor(dataMessage.getGameHistory());
        databaseProcessor.startReplay();
    }
    /**
     * communicates player's last move to the server
     * it has already been checked if it's legal
     * if it's returned as invalid something went deeply wrong
     */
    public void sendMove(Move move) throws InterruptedException {
        serverQueue.put(new MessageMoveFromClient(authenticationKey, move));
    }


    public void requestGameMode(GameType mode, int size) throws InterruptedException, IOException{
        switch (mode) {
            case PVP:
                serverQueue.put(new MessageGameRequestPVP(size));
                checkInbox();
                break;
            case BOT:
                
            default:
                break;
        }
        
    }

    public void requestDatabase() throws IOException, InterruptedException{
        serverQueue.put(new MessageDatabaseRequest());
        checkInbox();
        //get a response and call method in BoardCommunicator
        //DatabaseProcessor databaseProcessor = new DatabaseProcessor(response.gethistory);
        //databaseProcessor.startReplay
    }

}
