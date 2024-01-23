package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MoveGiveUp;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class GameState {
    /** Board object that stores information about the state of the game. */
    private Board board;

    /** Board object that stores information about the state of the game before the last move. */
    private Board previousBoard;

    /** Whose turn is it? Value can't be PieceType.EMPTY. */
    private PieceType turn;

    /** Keeps track of the score of each player. */
    private Map<PieceType, Integer> score;

    /** If not null, we won't allow any new moves to be made. */
    private PieceType winner;

    /** Starting position in this project's standard notation notation. */
    private String startingPosition; // TODO: implement this.



    // TODO: Add history of moves, score, etc.

    /**
     * GameState constructor.
     * @param boardSize
     */
    public GameState(final int boardSize) {
        this.board = new Board(boardSize);
        initializeGame();
    }

     /**
     * GameState constructor.
     * @param board
     */
    public GameState(final Board board, final PieceType turn) {
        this.board = board;
        initializeGame();
        this.turn = turn;
    }

    private void initializeGame() {
        score = new HashMap<PieceType, Integer>();
        this.score.put(PieceType.BLACK, 0);
        this.score.put(PieceType.WHITE, 0);

        this.turn = PieceType.BLACK;

        this.winner = null;
    }

    /**
     * Switches turn from black to white and vice versa.
     */
    private void switchTurn() {
        this.turn = this.turn.opposite();
    }


    /**
     * Makes a move on the board, checks if it's legal.
     * TODO: Add move to history.
     * @param move
     * @return MoveValidity. If move is legal, returns MoveValidity.LEGAL.
     */
    public MoveValidity makeMove(final Move move) {
        if (move instanceof MovePass) {
            this.switchTurn();
            return MoveValidity.LEGAL;
        }

        if (move instanceof MovePlace) {
            MovePlace movePlace = (MovePlace) move;

            PieceType pieceType = movePlace.getPieceType();
            Vector field = movePlace.getField();

            // check if move is legal. If not, return type of illegal move.
            MoveValidity moveValidity = this.isLegalMove(pieceType, field);
            if (!moveValidity.isLegal()) {
                return moveValidity;
            }

            // Update previous board.
            this.previousBoard = this.board.copy();

            // If move is legal, set the field to the piece type.
            this.board.setField(pieceType, field);
            List<Group> strangledGroups = this.board.groupsWithoutLiberties();

            // If there are captured groups, remove them from the board and update score.
            int score = 0;
            for (Group group : strangledGroups) {
                if (group.getPieceType() == pieceType.opposite()) {
                    score += group.size();
                    this.board.setFields(PieceType.EMPTY, group); // remove captured group from the board.
                }
            }

            // Update score.
            this.score.put(pieceType, this.score.get(pieceType) + score);

            this.switchTurn();

            return MoveValidity.LEGAL;
        }

        if (move instanceof MoveGiveUp) {
            MoveGiveUp moveGiveUp = (MoveGiveUp) move;

            PieceType pieceType = moveGiveUp.getPieceType();

            // Update previous board.
            this.previousBoard = this.board.copy();

            this.winner = pieceType.opposite();

            return MoveValidity.LEGAL;
        }

        throw new IllegalArgumentException("Unknown move type.");
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
    public MoveValidity isLegalMove(final PieceType pieceType, final Vector field) {
        // Check if piece type is valid.
        if (pieceType == PieceType.EMPTY) {
            return MoveValidity.EMPTY_PIECE;
        }

        // Check if it's a correct turn.
        if (!this.turn.equals(pieceType)) {
            return MoveValidity.WRONG_TURN;
        }

        // Check if field is on the board.
        if (field.getX() < 0 || field.getX() >= this.board.getBoardSize()) {
            return MoveValidity.OUTSIDE_BOARD;
        }
        if (field.getY() < 0 || field.getY() >= this.board.getBoardSize()) {
            return MoveValidity.OUTSIDE_BOARD;
        }

        // Check if field is empty.
        if (this.board.getField(field).isNotEmpty()) {
            return MoveValidity.FIELD_OCCUPIED;
        }


        // Instantiate a new board object.
        Board boardAfterMove = this.board.copy();

        // We forcibly set the field to the pieceType. We still don't know if the move is legal.
        boardAfterMove.setField(pieceType, field);

        // If after placing the piece, its group has at least one liberty, the move is legal.
        if (boardAfterMove.groupLiberties(boardAfterMove.group(field)).size() > 0) {
            return MoveValidity.LEGAL;
        }

        // Check if after placing the piece, there is a group with no liberties.
        List<Group> capturedGroups = boardAfterMove.groupsWithoutLiberties();

        // If there are no captured groups, the move is illegal.
        if (capturedGroups.isEmpty()) {
            return MoveValidity.SUICIDE;
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
            return MoveValidity.SUICIDE;
        }

        // If there are captured groups, remove them from the board.
        // ? Is it possible to capture more than one group at once ? I'm not sure, but regardless, this will work.
        for (Group group : capturedOpponentGroups) {
            boardAfterMove.setFields(PieceType.EMPTY, group);
        }

        // Ko rule. You can't repeat the previous board state.
        if (boardAfterMove.equals(this.previousBoard)) {
            return MoveValidity.REPETITION;
        }

        return MoveValidity.LEGAL;
    }

    /**
     * Getter for turn.
     * @return PieceType
     */
    public PieceType getTurn() {
        return this.turn;
    }

    /**
     * Override toString() method.
     * @return String
     */
    @Override
    public String toString() {
        return  "Turn: " + this.turn.longName()
                + "\tWhite: " + this.score.get(PieceType.WHITE)
                + "\tBlack: " + this.score.get(PieceType.BLACK)
                + "\n" + this.board.toString();
    }
}
