package com.gotp.server.messages;

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
}
