package com.gotp.server.messages.game_thread_messages;

import java.io.Serializable;

import com.gotp.server.messages.Message;
import com.gotp.server.messages.enums.MessageFunction;
import com.gotp.server.messages.enums.MessageTarget;
import com.gotp.server.messages.enums.MessageType;

public class MessageGameStoppedAbruptly implements Message, Serializable {
    /**
     * Get Target.
     * @return MessageTarget
     */
    @Override
    public MessageTarget getTarget() {
        return MessageTarget.CLIENT;
    }

    /**
     * Get Function.
     * @return MessageFunction
     */
    @Override
    public MessageFunction getFunction() {
        return MessageFunction.NOTIFICATION;
    }

    /**
     * Get Type.
     * @return MessageType
     */
    @Override
    public MessageType getType() {
        return MessageType.GAME_STOPPED_ABRUPTLY;
    }
}
