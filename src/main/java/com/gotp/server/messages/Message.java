package com.gotp.server.messages;

import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageType;
import com.gotp.server.messages.enums.MessageTarget;

public interface Message {
    /**
     * What is the purpose of the message?
     * @return The function of the message.
    */
    MessageFunction getFunction();

    /**
     * What type of message is it?
     * @return The type of message.
    */
    MessageType getType();

    /**
     * Get target of the message.
     * @return The target of the message.
     */
    MessageTarget getTarget();
}
