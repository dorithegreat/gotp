package com.gotp.server_example;

import java.io.Serializable;

public class SimpleMessage implements Serializable {
    /** message to be sent. */
    private String message;

    /**
     * Constructor.
     * @param message message to be sent
     */
    public SimpleMessage(final String message) {
        this.message = message;
    }

    /**
     * Getter for message.
     * @return message
     */
    public String getMessage() {
        return this.message;
    }
}
