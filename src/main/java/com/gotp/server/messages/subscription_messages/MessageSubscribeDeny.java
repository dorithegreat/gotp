package com.gotp.server.messages.subscription_messages;

import java.io.Serializable;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageSubscribeDeny implements Message, Serializable {
    /**
     * Get the type of the message.
     */
    @Override
    public MessageType getType() {
        return MessageType.SUBSCRIBE_DENY;
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
}
