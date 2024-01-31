package com.gotp.server;

import java.io.IOException;

import com.gotp.GUIcontrollers.BoardController;
import com.gotp.GUIcontrollers.DisplayBoard;
import com.gotp.GUIcontrollers.EndScreenController;
import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MoveGiveUp;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;
import com.gotp.server.Client.GameType;
import com.gotp.server.messages.game_thread_messages.MessageGameOver;

import javafx.concurrent.Task;

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
     * controller for the end screen.
     * needed because that's where database requests happen
     */
    private EndScreenController endScreenController;

    /**
     * the object storing board pieces (GUI components) and their logic.
     * bypasses the board controller for easier communication
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


    /**
     * empty constructor, but specifically private (it's a singleton)
     */
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
     * handles passing and resigning
     * the name is a bit misleading for historic reasons (this method went through a lot)
     * @param message type of message to be sent
     */
    public void send(String message) throws InterruptedException, IOException {
        MoveValidity validity;
        switch (message) {
            case "pass":
                validity = state.makeMove(new MovePass(player));
                if (validity == MoveValidity.LEGAL) {
                    client.sendMove(new MovePass(player));
                }
                else {
                    System.out.println("pass: " + validity);
                }
                break;

            case "resign":
                validity = state.makeMove(new MoveGiveUp(player));
                if (validity == MoveValidity.LEGAL) {
                    client.sendMove(new MoveGiveUp(player));   
                }
                else {
                    System.out.println("resign: " + validity);
                }
                break;

            default:
                break;
        }
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException, IOException{
                client.checkInbox();
                return null;
            }
        };
        new Thread(task).start();
    }

    /**
     * forwards a game request to the client, specifying its type
     * @param mode requested game mode (PVP or BOT)
     * @param n size of the board
     * @throws InterruptedException
     * @throws IOException
     */
    public void sendGameRequest(String mode, int n) throws InterruptedException, IOException {
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

    /**
     * will show current points of both players.
     * I'll implement it if I have the time
     */
    public void updatePoints(){
        // TODO implement
    }

    /**
     * forwards a move to the client, but only if it's legal.
     * updates the board at least once every time it's called
     */
    public void checkValidity(Vector coords) throws InterruptedException, IOException{
        MoveValidity validity = state.makeMove(new MovePlace(coords, player));
        if (validity == MoveValidity.LEGAL) {
            client.sendMove(new MovePlace(coords, player));

            // * task, so that the long waiting for response doesn't block javafx from updating
            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws InterruptedException, IOException{
                    client.checkInbox();
                    return null;
                }
            };
            new Thread(task).start();
        }
        else {
            System.out.println(validity);
        }
        drawBoard();
    }


    /**
     * executes a move received from the server
     * @param move
     * @throws IOException
     */
    public void makeMove(Move move) throws IOException {
        state.makeMove(move);
        drawBoard();
    }

    /**
     * gets current state of the board from GameState and updates all pieces to match it.
     */
    public void drawBoard(){
        Board stateBoard = state.getBoardCopy();
        for (int i = 0; i < stateBoard.getBoardSize(); i++) {
            for (int j = 0; j < stateBoard.getBoardSize(); j++) {
                board.makeMove(new Vector(i, j), stateBoard.getField(i, j));
            }
        }
        
    }

    /**
     * ends the game when the server sends a message about it.
     */
    public void endGame (MessageGameOver message) throws IOException, InterruptedException {
        EndScreenController.Result result;
        if (message.getWinner() == player) {
            result = EndScreenController.Result.WON;
        }
        else {
            result = EndScreenController.Result.LOST;
        }
        boardController.swtichToEndScreen(result);
    }

    public void makeNewBoard(GameHistory history) throws IOException {
        state = new GameState(history.getStartingPosition(), history.getStartingTurn());
        endScreenController.changeToBoard(state.getBoardSize());
    }

    /**
     * forwards a request for the history of the last game to the client
     * @throws IOException
     * @throws InterruptedException
     */
    public void sendDatabaseRequest() throws IOException, InterruptedException {
        client.requestDatabase();
        System.out.println("sent to client");
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

    /**
     * sets the color of the player
     */
    public void setPlayer(PieceType color) {
        player = color;
        board.setPlayer(color);
    }

    /**
     * getter for player
     */
    public PieceType getPlayer() {
        return player;
    }
}
