package com.gotp.server.messages.enums;

public enum MessageFunction {

    /** Used when sending data without confirmation needed. */
    DATA_MESSAGE,

    /** Used when requesting some kind of data from the other party. */
    REQUEST,

    /** Used when responding to a request. */
    RESPONSE,

    /** Used when something goes wrong. */
    ERROR,

    /** Used when confirming something. */
    CONFIRMATION
}
