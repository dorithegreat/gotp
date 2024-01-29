package com.gotp.GUIcontrollers;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;
import com.gotp.server.BoardCommunicator;

import java.io.IOException;

import javafx.scene.layout.GridPane;

public class DisplayBoard extends GridPane{

    /**
     * array of pieces this board contains
     * gridpanes don't easily allow access to pieces at specific coordinates
     * so they are additionally stored in an easy to access way
     */
    private BoardPiece[][] pieces;

    /**
     * communicator class for sending messages to the client
     */
    private BoardCommunicator communicator = BoardCommunicator.getInstance();

    /**
     * constructor. Populates the board with nxn grid of BoardPieces
     * @param n size of the board
     */
    public DisplayBoard(int n){
        pieces = new BoardPiece[n][n];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                BoardPiece piece = new BoardPiece();
                piece.linkToBoard(this);
                piece.setCoordinates(new Vector(i, j));
                add(piece, i, j);
                pieces[i][j] = piece;
            }
        }

        setGridLinesVisible(true);
    }

    /**
     * returns the color of the piece at specific coordinates
     * @param coords
     * @return
     */
    public PieceType getPieceColor(Vector coords) {
        return pieces[coords.getX()][coords.getY()].getCurrentColor();
    }

    public boolean check(Vector coordinates) throws IOException, ClassNotFoundException {
        return communicator.checkValidity(coordinates);
    }

    //delegates move to a specific stone that needs to change its color
    public void makeMove(Vector coords, PieceType color){
        pieces[coords.getX()][coords.getY()].makeMove(color);
    }
}
