package com.gotp.server;

import com.gotp.GUIcontrollers.BoardController;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

public final class BoardCommunicator {
    private static BoardCommunicator instance;
    private Client client;
    private BoardController boardController;

    private BoardCommunicator() {

    }

    public static synchronized BoardCommunicator getInstance() {
        if (instance == null) {
            instance = new BoardCommunicator();
        }
        return instance;
    }

    /**
     * relays a message from the board to the client, to be sent to the server.
     * @param message type of message to be sent
     */
    public void send(String message){
        client.sendToServer(message);
    }

    /**
     * sends a message to the board controller to update the state of one of the pieces.
     * @param vector coordinates of the piece
     * @param color color that the piece should be set to
     */
    public void updatePiece(Vector vector, PieceType color){
        // TODO implement this
    }

    public void updatePoints(){
        // TODO implement
    }


    //--------------------------------------------------------------
    //                  getters and setters
    //--------------------------------------------------------------

    /**
     * sets associated client
     * @param c the instance of Client
     */
    public void setClient(Client c) {
        client = c;
    }

    /**
     * sets the BoardController 
     * @param controller the controller
     */
    public void setBoard(BoardController controller){
        boardController = controller;
    }
    
    /**
     * getter for the BoardController
     * @return
     */
    public BoardController getBoard(){
        return boardController;
    }

    /**
     * getter for the Client
     * @return
     */
    public Client getClient(){
        return client;
    }

}
