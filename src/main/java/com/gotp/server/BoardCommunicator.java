package com.gotp.server;

import java.io.IOException;

import com.gotp.GUIcontrollers.BoardController;
import com.gotp.GUIcontrollers.DisplayBoard;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;
import com.gotp.server.Client.GameType;

/**
 * acts as a bridge between the client and the GUI part of the board
 * also validates moves passing through it
 * ! this class is a singleton
 */
public final class BoardCommunicator {
    /**
     * the only instance of this class (singleton).
     */
    private static BoardCommunicator instance;

    /**
     * the client this communicator communicates with.
     */
    private Client client;

    /**
     * the board controller responsible for the visual representation of the game.
     * this connection is not currently used but always could be
     */
    private BoardController boardController;

    /**
     * the object storing board pieces (GUI components) and their logic.
     * bypasses the board controller for easier communication
     * the controller is still needed though because it contains the 'pass' and 'resign' buttons
     * * THIS MIGHT END UP BEING REFACTORED
     */
    private DisplayBoard board;

    /**
     * stores all data about current state of the game. 
     * also processes moves.
     */
    private GameState state;

    /**
     * color of the player
     */
    private PieceType player;


    private BoardCommunicator() {

    }

    /**
     * the singleton method for getting the only valid instance of this class
     * @return
     */
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
    public void send(String message) throws IOException, ClassNotFoundException{
        switch (message) {
            case "pass":
                client.sendPass();
                break;

            case "resign":
                client.sendResign();
                break;

            default:
                break;
        }
    }

    public void sendGameRequest(String mode, int n) throws InterruptedException {
        if ("player".equals(mode)) {
            client.requestGameMode(GameType.PVP, n);
        }
        else if ("bot".equals(mode)) {
            client.requestGameMode(GameType.BOT, n);
        }
        else {
            System.out.println("requested a game mode that is neither PVP or bot");
        }
    }

    public void updatePoints(){
        // TODO implement
    }

    public void checkValidity(Vector coords) throws InterruptedException{
        MoveValidity validity = state.makeMove(new MovePlace(coords, player));
        if (validity == MoveValidity.LEGAL) {
            board.makeMove(coords, player);
            client.sendMove(new MovePlace(coords, player));
            client.processIncomingMove();
        }
        else{
            System.out.println("invalid move " + coords.getX() + " " + coords.getY() + " " + validity);
        }

    }

    public void makeMove(Move move){
        if (state.makeMove(move) == MoveValidity.LEGAL) {
            //the Move interface doesn't provide an easy way to differentiate between the types so instanceof it is
            if (move instanceof MovePlace) {
                MovePlace movePlace = (MovePlace) move;
                board.makeMove(movePlace.getField(), movePlace.getPieceType());
            }
            //else process pass or resign
        }
        else {
            System.out.println("Server sent an illegal move");
        }
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
    public void setBoardContrller(BoardController controller, int n){
        boardController = controller;
        state = new GameState(n);
    }
    
    public void setBoard(DisplayBoard board){
        this.board = board;
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

    public void setPlayer(PieceType color){
        player = color;
    }
}
