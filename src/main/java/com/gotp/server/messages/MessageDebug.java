package com.gotp.server.messages;

import java.io.Serializable;

import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageDebug implements Message, Serializable {
    /** Debug message. */
    private String debugMessage;

    /** Message target. */
    private MessageTarget target;

    /**
     * Constructor without target.
     * @param debugMessage Debug message.
     */
    public MessageDebug(final String debugMessage) {
        this.debugMessage = debugMessage;
        this.target = MessageTarget.UNSPECIFIED;
    }

    /**
     * Constructor with target.
     * @param debugMessage Debug message.
     * @param target Message target.
     */
    public MessageDebug(final String debugMessage, final MessageTarget target) {
        this.debugMessage = debugMessage;
        this.target = target;
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

    /**
     * Get the target of the message.
     * @return null
     */
    @Override
    public MessageTarget getTarget() {
        return this.target;
    }
}
