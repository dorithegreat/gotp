package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gotp.game_mechanics.utilities.Vector;

// TODO: finish implementing legal moves including capturing pieces.

public class GameState {
    /**
     * Board object that stores information about the state of the game.
     */
    private Board board;

    /**
     * Board object that stores information about the state of the game before the last move.
     */
    private Board previousBoard;

    /**
     * GameState constructor.
     * @param boardSize
     */
    public GameState(final int boardSize) {
        this.board = new Board(boardSize);
    }

    /**
     * A move is considered semilegla if the position is on the board and the field is not empty.
     * @param pieceType
     * @param field
     * @return boolean
     */
    public boolean isSemiLegalMove(final PieceType pieceType, final Vector field) {
        // Check if piece type is valid.
        if (pieceType == PieceType.EMPTY) {
            return false;
        }

        // Check if field is on the board.
        if (field.getX() < 0 || field.getX() >= this.board.getBoardSize()) {
            return false;
        }
        if (field.getY() < 0 || field.getY() >= this.board.getBoardSize()) {
            return false;
        }

        // Check if field is empty.
        return this.board.getField(field) != PieceType.EMPTY;
    }


    /**
     * A move is considered legal if 3 rules are satisfied:
     * 1. A field on witch 
     * TODO: finish implementing this method.
     * @param pieceType
     * @param field
     * @return boolean
     */
    public boolean isLegalMove(final PieceType pieceType, final Vector field) {
        
        // Instantiate a new board object.
        Board boardAfterMove = this.board.clone();

        // We forcibly set the field to the pieceType. We still don't know if the move is legal.
        boardAfterMove.setField(pieceType, field);

        // Check if capture is possible.
        List<Set<Vector>> capturedGroups = boardAfterMove.capturedGroups();

        if (capturedGroups.isEmpty()) {
            return false;
        }

        for (Set<Vector> hashSet : capturedGroups) {
            boardAfterMove.setFields(PieceType.EMPTY, hashSet);
        }

        // Ko rule. You can't repeat the previous board state.
        return boardAfterMove.equals(this.previousBoard);
    }
}
