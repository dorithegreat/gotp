package com.gotp.server.messages.game_thread_messages;

import java.io.Serializable;
import java.net.Socket;

import com.gotp.game_mechanics.board.move.Move;
import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageWithSocket;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;


public class MessageMoveFromClient implements Message, MessageWithSocket, Serializable {

    /** Client's socket. */
    private final Socket clientSocket;

    /** Move to send. */
    private final Move move;

    /**
     * Constructor.
     * @param clientSocket
     * @param move
     */
    public MessageMoveFromClient(final Socket clientSocket, final Move move) {
        this.clientSocket = clientSocket;
        this.move = move;
    }

    /**
     * Get Socket.
     * @return Socket
     */
    @Override
    public Socket getClientSocket() {
        return this.clientSocket;
    }

    /**
     * Add Socket.
     * @param clientSocket
     * @return Message
     */
    @Override
    public Message addClientSocket(final Socket clientSocket) {
        return new MessageMoveFromClient(clientSocket, this.move);
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
}
