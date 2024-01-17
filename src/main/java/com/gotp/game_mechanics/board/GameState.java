package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.List;

import com.gotp.game_mechanics.utilities.Vector;

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
     * GameState constructor.
     * @param board
     */
    public GameState(final Board board) {
        this.board = board;
    }

    /**
     * A move is considered legal if 3 rules are satisfied:
     * 1. A field on witch the piece is placed is empty.
     * 2. After placing the piece, it has at least one liberty
     * (this includes situation when it captures opponent's pieces).
     * 3. The move does not repeat the previous board state (Ko rule).
     * @param pieceType
     * @param field
     * @return boolean
     */
    public boolean isLegalMove(final PieceType pieceType, final Vector field) {
        // Check if piece type is valid.
        if (pieceType == PieceType.EMPTY) {
            System.out.println("Failed! Piece type is EMPTY.");
            return false;
        }

        // Check if field is on the board.
        if (field.getX() < 0 || field.getX() >= this.board.getBoardSize()) {
            System.out.println("Failed! Field is not on the board.");
            return false;
        }
        if (field.getY() < 0 || field.getY() >= this.board.getBoardSize()) {
            return false;
        }

        // Check if field is empty.
        if (this.board.getField(field).isNotEmpty()) {
            System.out.println("Failed! Field is not empty.");
            return false;
        }


        // Instantiate a new board object.
        Board boardAfterMove = this.board.clone();

        // We forcibly set the field to the pieceType. We still don't know if the move is legal.
        boardAfterMove.setField(pieceType, field);

        // Check if after placing the piece, there is a group with no liberties.
        List<Group> capturedGroups = boardAfterMove.groupsWithoutLiberties();

        // If there are no captured groups, the move is illegal.
        if (capturedGroups.isEmpty()) {
            return false;
        }

        // Filter own groups from captured groups.
        // We don't have to check if we may strangle our own group,
        // because if the piece we are currently checking will have at least one liberty,
        // then the entire group will have at least one liberty.
        List<Group> capturedOpponentGroups = new ArrayList<Group>();
        for (Group group : capturedGroups) {
            if (group.getPieceType() == pieceType.opposite()) {
                capturedOpponentGroups.add(group);
            }
        }
        if (capturedOpponentGroups.isEmpty()) {
            return false;
        }

        // If there are captured groups, remove them from the board.
        // ? Is it possible to capture more than one group at once ? I'm not sure, but regardless, this will work.
        for (Group group : capturedOpponentGroups) {
            boardAfterMove.setFields(PieceType.EMPTY, group);
        }

        // Ko rule. You can't repeat the previous board state.
        return !(boardAfterMove.equals(this.previousBoard));
    }
}
