package com.gotp.GUIcontrollers;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

import javafx.scene.layout.GridPane;

public class DisplayBoard extends GridPane{

    /**
     * array of pieces this board contains
     * gridpanes don't easily allow access to pieces at specific coordinates
     * so they are additionally stored in an easy to access way
     */
    private BoardPiece[][] pieces;

    /**
     * constructor. Populates the board with nxn grid of BoardPieces
     * @param n size of the board
     */
    public DisplayBoard(int n){
        pieces = new BoardPiece[n][n];

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                BoardPiece piece = new BoardPiece();
                add(piece, i, j);
                pieces[i][j] = piece;
            }
        }
    }

    /**
     * returns the color of the piece at specific coordinates
     * @param coords
     * @return
     */
    public PieceType getPieceColor(Vector coords) {
        return pieces[coords.getX()][coords.getY()].getCurrentColor();
    }

    public void notifyOfChange(Vector coordinates, PieceType color){
        // TODO link with a controller or communicator and send a message to the server
    }
}
