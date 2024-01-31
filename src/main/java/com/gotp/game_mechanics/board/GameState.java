package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MoveGiveUp;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

/**
 * This class represents state of the Game.
 * It's a Facade for Borad class.
 * It can modyfy the board, only by making legal moves.
 * It also keeps track of the score and history of the game.
 */
public class GameState {
    /** Board object that stores information about the state of the game. */
    private Board board;

    /** Board object that stores information about the state of the game before the last move. */
    private Board previousBoard;

    /** Whose turn is it? Value can't be PieceType.EMPTY. */
    private PieceType turn;

    /** Keeps track of the score of each player. */
    private Map<PieceType, Double> score;

    /** History of every move. */
    private GameHistory history;

    /**
     * GameState constructor.
     * Creates a new Game with empty board with size `boardSize`.
     * @param boardSize
     */
    public GameState(final int boardSize) {
        this.board = new Board(boardSize);
        initializeGame();
    }

     /**
     * GameState constructor.
     * Creates a new Game with an existing board.
     * @param board
     * @param turn
     */
    public GameState(final Board board, final PieceType turn) {
        this.board = board;
        initializeGame();
        this.turn = turn;
    }

    /**
     * GameState constructor.
     * Creates a new Game with a standard String representation of the board.
     * @param standardBoardString
     * @param turn
     */
    public GameState(final String standardBoardString, final PieceType turn) {
        this.board = new Board(standardBoardString);
        initializeGame();
        this.turn = turn;
    }

    /**
     * GameState constructor.
     * Mainly used for testing and copy method.
     * @param board
     * @param previousBoard
     * @param turn
     * @param score
     * @param history
     */
    public GameState(
            final Board board,
            final Board previousBoard,
            final PieceType turn,
            final Map<PieceType, Double> score,
            final GameHistory history
    ) {
        this.board = board;
        this.previousBoard = previousBoard;
        this.turn = turn;
        this.score = score;
        this.history = history;
    }

    /**
     * Initializes some game variables.
     */
    private void initializeGame() {
        score = new HashMap<PieceType, Double>();

        final double komi = 6.5;

        this.score.put(PieceType.BLACK, 0d);
        this.score.put(PieceType.WHITE, komi);

        this.turn = PieceType.BLACK;

        this.history = new GameHistory(this.turn, this.board.toString());
        this.history.setWinner(null);
    }

    /**
     * Switches turn from black to white and vice versa.
     */
    private void switchTurn() {
        this.turn = this.turn.opposite();
    }

    /**
     * Makes a move on the board, checks if it's legal.
     * @param move
     * @return MoveValidity. If move is legal, returns MoveValidity.LEGAL.
     */
    public MoveValidity makeMove(final Move move) {
        if (move instanceof MovePass) {
            this.switchTurn();

        } else if (move instanceof MovePlace) {
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

        } else if (move instanceof MoveGiveUp) {
            MoveGiveUp moveGiveUp = (MoveGiveUp) move;

            // PieceType pieceType = moveGiveUp.getPieceType();

            // Update previous board.
            this.previousBoard = this.board.copy();

        } else {
            // should never happen.
            throw new IllegalArgumentException("Unknown move type.");
        }

        history.add(move);
        return MoveValidity.LEGAL;
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
     * True if last to moves were passes.
     * @return boolean
     */
    public boolean doublePass() {
        if (this.history.size() < 2) {
            return false;
        }
        // Get last element of this.history
        Move lastMove = this.history.get(this.history.size() - 1);
        Move previousMove = this.history.get(this.history.size() - 2);

        return lastMove instanceof MovePass && previousMove instanceof MovePass;
    }

    /**
     * True if a player gaveUp.
     * @return boolean
     */
    public boolean playerGaveUp() {
        if (getHistory().size() == 0) {
            return false;
        }

        // Is last move a MoveGiveUp?
        return getHistory().get(getHistory().size() - 1) instanceof MoveGiveUp;
    }

    /**
     * True if doublePass() or playerGaveUp().
     * @return boolean
     */
    public boolean gameOver() {
        return doublePass() || playerGaveUp();
    }

    /**
     * Returns an overall score.
     * @return Overall score for everything.
     */
    public Map<PieceType, Double> overallScore() {
        // points from captured groups
        double whiteScore = this.score.get(PieceType.WHITE);
        double blackScore = this.score.get(PieceType.BLACK);

        // points from placed pieces
        List<Group> groups = this.board.groups();
        for (Group group : groups) {
            if (group.getPieceType() == PieceType.WHITE) {
                whiteScore += group.size();
            } else {
                blackScore += group.size();
            }
        }

        // add points for territories
        List<Set<Vector>> territories = this.board.allTerritories();
        PieceType territoryOwner;
        for (Set<Vector> territory : territories) {
            territoryOwner = this.board.territoryOwner(territory);
            if (territoryOwner == PieceType.WHITE) {
                whiteScore += territory.size();
            } else if (territoryOwner == PieceType.BLACK) {
                blackScore += territory.size();
            }
        }

        Map<PieceType, Double> overallScore = new HashMap<>();
        overallScore.put(PieceType.WHITE, whiteScore);
        overallScore.put(PieceType.BLACK, blackScore);

        // If player gave up, give him -1 point.
        if (playerGaveUp()) {
            Move lastMove = this.history.get(this.history.size() - 1);
            PieceType pieceType = lastMove.getPieceType();
            overallScore.put(pieceType, -1.0);
        }

        return overallScore;
    }

    /**
     * Getter for boardSize.
     * @return boardSize
     */
    public int getBoardSize() {
        return this.board.getBoardSize();
    }

    /**
     * Getter for `board`.
     * * This can't return reference to `this.board`, cause someone could modyfy it.
     * @return Board
     */
    public Board getBoardCopy() {
        return this.board.copy();
    }

    /**
     * Copy of the game state.
     * Without history.
     * @return GameState
     */
    public GameState copy() {
        HashMap<PieceType, Double> scoreCopy = new HashMap<>();
        scoreCopy.put(PieceType.WHITE, this.score.get(PieceType.WHITE));
        scoreCopy.put(PieceType.BLACK, this.score.get(PieceType.BLACK));

        return new GameState(
                this.board.copy(),
                this.previousBoard.copy(),
                this.turn,
                scoreCopy,
                (GameHistory) this.history.clone()
        );
    }

    /**
     * Getter for turn.
     * @return PieceType
     */
    public PieceType getTurn() {
        return this.turn;
    }

    /**
     * Returns `this.history`.
     * @return GameHistory
     */
    public GameHistory getHistory() {
        return this.history;
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
