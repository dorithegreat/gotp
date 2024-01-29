package com.gotp.server.messages.game_thread_messages;

import java.io.Serializable;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageGameStarted implements Message, Serializable {
    /** Authentication key so that users can't be impersonated. */
    private final int authenticationKey;

    /** Board size. */
    private final int boardSize;

    /** piece type player got. */
    private final PieceType playerPieceType;

    /**
     * Constructor.
     * @param authenticationKey
     * @param boardSize
     * @param playerPieceType
     */
    public MessageGameStarted(final int boardSize, final int authenticationKey, final PieceType playerPieceType) {
        this.boardSize = boardSize;
        this.authenticationKey = authenticationKey;
        this.playerPieceType = playerPieceType;
    }

    /**
     * Get authentication key.
     * @return int
     */
    public int getAuthenticationKey() {
        return this.authenticationKey;
    }

    /**
     * Get board size.
     * @return int
     */
    public int getBoardSize() {
        return this.boardSize;
    }


    // ---------------------------- Message interface ----------------------------

    /**
     * Get player piece type.
     * @return PieceType
     */
    public PieceType getPlayerPieceType() {
        return this.playerPieceType;
    }

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
        return MessageFunction.DATA_MESSAGE;
    }

    /**
     * Get Type.
     * @return MessageType
     */

    @Override
    public MessageType getType() {
        return MessageType.GAME_STARTED;
    }
}
