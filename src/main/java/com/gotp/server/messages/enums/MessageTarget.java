package com.gotp.server.messages.enums;

public enum MessageTarget {
    /** Exchanging moves etc. */
    GAME_THREAD,

    /** Client. */
    CLIENT,

    /** Requesting games and playbacks. */
    SERVER_THREAD,

    /** Subscribtions and debugging. */
    FORWARDER,

    /** Unspecified thread. */
    UNSPECIFIED
}
