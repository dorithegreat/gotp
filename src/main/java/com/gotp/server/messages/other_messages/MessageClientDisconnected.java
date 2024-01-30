package com.gotp.server.messages.other_messages;

import java.io.Serializable;
import java.net.Socket;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.MessageWithSocket;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.server_thread_messages.MessageGameRequestPVP;

public class MessageClientDisconnected implements Message, Serializable, MessageWithSocket {
    /**
     * Get the message type.
     * @return MessageType
     */
    @Override
    public MessageType getType() {
        return MessageType.CLIENT_DISCONNECTED;
    }

    /**
     * Get the message target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.FORWARDER;
    }

    /**
     * Get message function.
     * @return MessageFunction
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.NOTIFICATION;
    }

    /** Socket to delete from shared recources. */
    private final Socket clientSocket;

    /**
     * Constructor with Socket.
     * @param clientSocket
     */
    public MessageClientDisconnected(final Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Constructor without socket.
     */
    public MessageClientDisconnected() {
        this.clientSocket = null;
    }

    /**
     * Get the message's socket.
     * @return Socket
     */
    @Override
    public Socket getClientSocket() {
        return this.clientSocket;
    }

    /**
     * Add socket to the message.
     * @return Message
     */
    @Override
    public Message addClientSocket(final Socket socket) {
        return new MessageClientDisconnected(socket);
    }
}
