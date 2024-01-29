package com.gotp.server.messages.server_thread_messages;

import java.io.Serializable;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

/**
 * Message sent to client when a PVP game request is successful
 * and was put on a waitlist.
 */
public class MessageGameRequestPVPFailed implements Message, Serializable {
    /**
     * Get message function.
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.RESPONSE;
    }

    /**
     * Get message type.
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_REQUEST_FAIL;
    }

    /**
     * Get target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }

    /** Reason for failure. */
    private final String reason;

    /**
     * Constructor.
     * @param reason
     */
    public MessageGameRequestPVPFailed(final String reason) {
        this.reason = reason;
    }

    /**
     * Get reason.
     * @return String
     */
    public String getReason() {
        return reason;
    }
}
