package com.gotp.server.messages.subscription_messages;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageSubscribeRequest implements Message, Serializable {
    /** ObjectOutputStream. */
    private BlockingQueue<Message> threadQueue;

    /**
     * Get the type of the message.
     */
    @Override
    public MessageType getType() {
        return MessageType.SUBSCRIBE_REQUEST;
    }

    /**
     * Get the function of the message.
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.REQUEST;
    }

    /**
     * Get the target of the message.
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.FORWARDER;
    }

    /**
     * Constructor.
     * @param threadQueue
     */
    public MessageSubscribeRequest(final BlockingQueue<Message> threadQueue) {
        this.threadQueue = threadQueue;
    }

    /**
     * Getter for out.
     * @return ObjectOutputStream
     */
    public BlockingQueue<Message> getThreadQueue() {
        return this.threadQueue;
    }
}
