package com.gotp.server.messages;

import java.io.Serializable;

import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageType;

public class MessageDebug implements Message, Serializable {
    /** Debug message. */
    private String debugMessage;

    /**
     * Constructor.
     * @param debugMessage Debug message.
     */
    public MessageDebug(final String debugMessage) {
        this.debugMessage = debugMessage;
    }

    /**
     * Get the debug message.
     * @return Debug message.
     */
    public String getDebugMessage() {
        return debugMessage;
    }

    /**
     * Get the debug message.
     * @return MessageFunction.DATA_MESSAGE
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.DATA_MESSAGE;
    }

    /**
     * Get the type of message.
     * @return MessageType.DEBUG
     */
    public MessageType getType() {
        return MessageType.DEBUG;
    }
}
