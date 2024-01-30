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
import com.gotp.server.messages.Message;
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
     * basically an ID of this player
     */
    private int authenticationKey;

    private BlockingQueue<Message> serverQueue;
    private BlockingQueue<Message> receivedQueue;


    // * Filip put this here "to calm down pmd" and I'm not sure what for but I have no reason to delete it
    /** Private constructor. Disallow instantiation. */
    public Client() { }

    /**
     * Main method.
     */
    public void start(Stage stage) throws IOException {

        Scene scene = new Scene(loadFXML("testScene"));
        stage.setScene(scene);
        stage.show();

        board = BoardCommunicator.getInstance();
        board.setClient(this);
        board.setPlayer(PieceType.BLACK);
        
        serverQueue = new LinkedBlockingDeque<>();
        receivedQueue = new LinkedBlockingDeque<>();
        ClientThread clientThread = new ClientThread(serverQueue, receivedQueue);
        //ReceiverThread receiverThread = new ReceiverThread(receivedQueue);
        new Thread(clientThread).start();
        //new Thread(receiverThread).start();
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
    public void sendMove(Move move) throws InterruptedException {
        serverQueue.put(new MessageMoveFromClient(authenticationKey, move));

        //check server's response here
        //currently the server doesn't send any response in return so there's nothing to test

        // Message response = receivedQueue.take();
        // // *needs to be changed to MOVE_FROM_SERVER but that type of message doesn't yet exist
        // if (response.getFunction() == MessageFunction.DATA_MESSAGE && response.getType() == MessageType.MOVE_FROM_CLIENT) {
        //     Move serverMove = ((MessageMoveFromServer) response).getMove();   
        // }
    }

    /**
     * sends a message to the server that the user wants to pass
     */
    public void sendPass() throws IOException, ClassNotFoundException{
        //server.send(/*pass message */);
        //Message response = server.receive();
        //process the response
    }

    /**
     * sends a message that the user wants to immediately lose the game and disconnect
     */
    public void sendResign() throws IOException, ClassNotFoundException{
        //serverQueue.put();
        //Message response = server.receive();
        //process the response
    }

    public void requestGameMode(GameType mode, int size) throws InterruptedException{
        switch (mode) {
            case PVP:
                serverQueue.put(new MessageGameRequestPVP(size));
                break;
            case BOT:
                
            default:
                break;
        }
        Message response = receivedQueue.take();
        //System.out.println("response received");

        // ? the massive amount of comments is because I'm fairly sure the server doesn't send GAME_REQUEST_SUCCESS
        // ? but I'm not sure if that's how it will be in the end so I'm leaving the code here just in case

        // if (response.getFunction() == MessageFunction.RESPONSE && response.getType() == MessageType.GAME_REQUEST_SUCCESS) {
        //     System.out.println("game request approved");
        //     response = receivedQueue.take();
            if (response.getType() == MessageType.GAME_STARTED) {
                System.out.println("game started");
                //ugly but will stay for now

                MessageGameStarted gameStarted = (MessageGameStarted) response;
                authenticationKey = gameStarted.getAuthenticationKey();
                board.setPlayer(gameStarted.getPlayerPieceType());
                if (gameStarted.getPlayerPieceType() == PieceType.WHITE) {
                    processIncomingMove();
                }
            }
            //server registered game request as valid but returned something else than Game Started
           // System.out.println("Game could not be started");
        // }
        // else{
        //     System.out.println("game request denied");
        //     return;
        // }
        
    }

    public void requestDatabase(){
        //send an appropriate message
        //get a response and call method in BoardCommunicator
    }

    public void processIncomingMove() throws InterruptedException{
        Message response = receivedQueue.take();
        System.out.println("received a response");
        
        //check if it's game ended message

        // * should be move from server but this is how it is on the server
        if (response.getType() == MessageType.MOVE_FROM_CLIENT) {
            MessageMoveFromServer moveMessage = (MessageMoveFromServer) response;
            board.makeMove(moveMessage.getMove());
        }
    }
}
