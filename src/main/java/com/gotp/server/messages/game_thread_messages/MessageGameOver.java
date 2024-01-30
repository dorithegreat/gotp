package com.gotp.server.messages.game_thread_messages;

import java.io.Serializable;
import java.util.Map;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageGameOver implements Message, Serializable {
    /** Who won the game? */
    private final PieceType winner;

    /** Final score. */
    private final Map<PieceType, Double> finalScore;

    /**
     * Constructor.
     * @param winner
     * @param finalScore
     */
    public MessageGameOver(final PieceType winner, final Map<PieceType, Double> finalScore) {
        this.winner = winner;
        this.finalScore = finalScore;
    }

    /**
     * Get winner.
     * @return PieceType
     */
    public PieceType getWinner() {
        return this.winner;
    }

    /**
     * Get final score.
     * @return Map<PieceType, Double>
     */
    public Map<PieceType, Double> getFinalScore() {
        return this.finalScore;
    }



    // ---------------------------- Message interface ----------------------------

    /**
     * Get Target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }

    /**
     * Get Function.
     * @return MessageFunction
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.NOTIFICATION;
    }

    /**
     * Get Type.
     * @return MessageType
     */

    @Override
    public MessageType getType() {
        return MessageType.GAME_OVER;
    }
}
