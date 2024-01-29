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
public class MessageGameRequestPVPSuccess implements Message, Serializable {
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
        return MessageType.GAME_REQUEST_SUCCESS;
    }

    /**
     * Get target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }
}
