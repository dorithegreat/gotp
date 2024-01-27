package com.gotp.server.messages;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageType;

public class MessageSubscribe implements Message, Serializable {
    /** ObjectOutputStream. */
    private BlockingQueue<Message> out;

    /**
     * Get the type of the message.
     */
    @Override
    public MessageType getType() {
        return MessageType.DEBUG;
    }

    /**
     * Get the function of the message.
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.REQUEST;
    }

    /**
     * Constructor.
     * @param out
     */
    public MessageSubscribe(final BlockingQueue<Message> out) {
        this.out = out;
    }

    /**
     * Getter for out.
     * @return ObjectOutputStream
     */
    public BlockingQueue<Message> getOut() {
        return out;
    }
}
