package com.gotp.game_mechanics.board;

import com.gotp.game_mechanics.board.move.Move;

import java.util.ArrayList;
import java.util.Map;

public class GameHistory extends ArrayList<Move> {

    /** Starting turn. */
    private PieceType startingTurn;

    /** Starting position. */
    private String startingPosition;

    /** Who won the game? */
    private PieceType winner;

    /** Final score of each player. */
    private Map<PieceType, Double> finalScore;

    /**
     * GameHistory constructor.
     * @param startingTurn
     * @param startingPosition
     */
    GameHistory(final PieceType startingTurn, final String startingPosition) {
        super();
        this.startingTurn = startingTurn;
        this.startingPosition = startingPosition;
    }

    /**
     * Getter for starting turn.
     * @return startingTurn
     */
    public PieceType getStartingTurn() {
        return startingTurn;
    }

    /**
     * Getter for starting position.
     * @return startingPosition
     */
    public String getStartingPosition() {
        return startingPosition;
    }

    /**
     * Getter for winner.
     * @return winner
     */
    public PieceType getWinner() {
        return winner;
    }

    /**
     * Getter for final score.
     * @return finalScore
     */
    public Map<PieceType, Double> getFinalScore() {
        return finalScore;
    }

    /**
     * Setter for winner.
     * @param winner
     */
    public void setWinner(final PieceType winner) {
        this.winner = winner;
    }

    /**
     * Setter for final score.
     * @param finalScore
     */
    public void setFinalScore(final Map<PieceType, Double> finalScore) {
        this.finalScore = finalScore;
    }


}
