package com.gotp.server.messages.other_messages;

import java.io.Serializable;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageServerDisconnected implements Message, Serializable {
    /**
     * Get the message type.
     * @return MessageType
     */
    @Override
    public MessageType getType() {
        return MessageType.SERVER_DISCONNECTED;
    }

    /**
     * Get the message target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }

    /**
     * Get message function.
     * @return MessageFunction
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.NOTIFICATION;
    }
}
