package com.gotp.server.messages.enums;

public enum MessageType {
    /** Methods for threads to subscribe to client's messages. */
    SUBSCRIBE_REQUEST, SUBSCRIBE_ACCEPT, SUBSCRIBE_DENY,

    /** Requesting games from the serverThread. */
    GAME_REQUEST_PVP, GAME_REQUEST_BOT, GAME_REQUEST_SUCCESS, GAME_REQUEST_FAIL,

    /** Game thread messages. */
    GAME_STARTED, MOVE_FROM_CLIENT, MOVE_FROM_SERVER,

    /** Debug. */
    DEBUG,

    /** Connection related types. */
    CLIENT_DISCONNECTED, SERVER_DISCONNECTED
}
