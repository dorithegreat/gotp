package com.gotp.server.players;

import com.gotp.server.messages.Message;

public interface Player {
    /**
     *  To send messages to the player.
     * @param message The message to send.
    */
    void send(Message message);

    /**
     * To receive messages from the player.
     * @return The message received.
    */
    Message receive();
}
