package com.gotp.server.messages.database_messages;

import java.io.Serializable;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

/**
 * For now let's only consider
 */
public class MessageDatabaseRequestSpecific implements Message, Serializable {
    /**
     * Get the message function.
     * @return MessageFunction.DATA_MESSAGE
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.REQUEST;
    }

    /**
     * Get the type of message.
     * @return MessageType.DEBUG
     */
    public MessageType getType() {
        return MessageType.DATABASE_REQUEST_SPECIFIC;
    }

    /**
     * Get the target of the message.
     * @return null
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.SERVER_THREAD;
    }

    /**
     * Get the id of the message.
     */
    private int id;

    /**
     * Constructor.
     * @param id
     */
    public MessageDatabaseRequestSpecific(final int id) {
        this.id = id;
    }

    /**
     * Get the id of the message.
     * @return id
     */
    public int getId() {
        return id;
    }

}
