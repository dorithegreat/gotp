package com.gotp.server.messages.database_messages;

import java.io.Serializable;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageDatabaseResponse implements Message, Serializable {
    /**
     * Get the message function.
     * @return MessageFunction.DATA_MESSAGE
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.RESPONSE;
    }

    /**
     * Get the type of message.
     * @return MessageType.DEBUG
     */
    public MessageType getType() {
        return MessageType.DATABASE_RESPONSE;
    }

    /**
     * Get the target of the message.
     * @return null
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }

    /** GameHistory, client requested. */
    private GameHistory gameHistory;

    /**
     * Constructor.
     * @param gameHistory
     */
    public MessageDatabaseResponse(final GameHistory gameHistory) {
        this.gameHistory = gameHistory;
    }

    /**
     * Get the game history.
     * @return GameHistory
     */
    public GameHistory getGameHistory() {
        return gameHistory;
    }
}
