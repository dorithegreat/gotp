package com.gotp.server.messages.game_thread_messages;

import java.io.Serializable;

import com.gotp.game_mechanics.board.move.Move;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;


public class MessageMoveFromClient implements Message, Serializable {

    /** Client's authentication key. */
    private int authenticationKey;

    /** Move to send. */
    private final Move move;

    /**
     * Constructor.
     * @param authenticationKey
     * @param move
     */
    public MessageMoveFromClient(final int authenticationKey, final Move move) {
        this.authenticationKey = authenticationKey;
        this.move = move;
    }

    /**
     * Get Target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.GAME_THREAD;
    }

    /**
     * Get Function.
     * @return MessageFunction
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.DATA_MESSAGE;
    }

    /**
     * Get Type.
     * @return MessageType
     */

    @Override
    public MessageType getType() {
        return MessageType.MOVE_FROM_CLIENT;
    }

    /**
     * Get Move.
     * @return Move
     */
    public Move getMove() {
        return this.move;
    }

    /**
     * Get Authentication Key.
     * @return int
     */
    public int getAuthenticationKey() {
        return this.authenticationKey;
    }
}
