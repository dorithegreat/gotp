package com.gotp.server.messages.server_thread_messages;

import java.io.Serializable;
import java.net.Socket;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageWithSocket;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

/**
 * Request a PVP game from the server thread.
 */
public class MessageGameRequestPVP implements Message, Serializable, MessageWithSocket {
    /** Size of the requested game. */
    private final int boardSize;

    /** Client who requested the game. */
    private final Socket requestingClient;

    /**
     * Get message function.
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.REQUEST;
    }

    /**
     * Get message type.
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_REQUEST_PVP;
    }

    /**
     * Get target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.SERVER_THREAD;
    }

    /**
     * Constructor.
     * @param boardSize
     */
    public MessageGameRequestPVP(final int boardSize) {
        if (boardSize < 1) {
            throw new IllegalArgumentException("Game size must be at least 1.");
        }
        this.boardSize = boardSize;
        this.requestingClient = null;
    }

    /**
     * Constructor.
     * @param boardSize
     * @param requestingClient
     */
    public MessageGameRequestPVP(final Socket requestingClient, final int boardSize) {
        if (boardSize < 1) {
            throw new IllegalArgumentException("Game size must be at least 1.");
        }
        this.boardSize = boardSize;
        this.requestingClient = requestingClient;
    }

    /**
     * Get game size.
     * @return int
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Get requesting client.
     * @return Socket
     */
    public Socket getRequestingClient() {
        return requestingClient;
    }

    /**
     * Get socket.
     * @return Socket
     */
    @Override
    public Socket getClientSocket() {
        return this.requestingClient;
    }

    /**
     * Add socket to the message.
     * @return Message
     */
    @Override
    public Message addClientSocket(final Socket socket) {
        return new MessageGameRequestPVP(socket, this.boardSize);
    }
}
