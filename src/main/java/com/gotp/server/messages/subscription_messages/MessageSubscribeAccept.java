package com.gotp.server.messages.subscription_messages;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageSubscribeAccept implements Message, Serializable {
    /** ObjectOutputStream. */
    private BlockingQueue<Message> threadQueue;

    /**
     * Get the type of the message.
     */
    @Override
    public MessageType getType() {
        return MessageType.SUBSCRIBE_ACCEPT;
    }

    /**
     * Get the function of the message.
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.RESPONSE;
    }

    /**
     * Get the target of the message.
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.UNSPECIFIED;
    }

    /**
     * Getter for out.
     * @return ObjectOutputStream
     */
    public BlockingQueue<Message> getThreadQueue() {
        return this.threadQueue;
    }
}
