package com.gotp.server;

import java.net.Socket;
import java.util.Objects;

public class GameRequest {
    /** Requested board size. */
    private final int boardSize;

    /** Client who requested the game. */
    private final Socket requestingClient;

    /**
     * Constructor.
     * @param boardSize
     * @param requestingClient
     */
    public GameRequest(final Socket requestingClient, final int boardSize) {
        if (boardSize < 1) {
            throw new IllegalArgumentException("Game size must be at least 1.");
        }
        this.boardSize = boardSize;
        this.requestingClient = requestingClient;
    }

    /**
     * Get game size.
     * @return int
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Get requesting client.
     * @return Socket
     */
    public Socket getRequestingClient() {
        return requestingClient;
    }

    /**
     * Override equals.
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof GameRequest)) {
            return false;
        }
        GameRequest otherGameRequest = (GameRequest) other;
        return this.boardSize == otherGameRequest.boardSize
            && this.requestingClient.equals(otherGameRequest.requestingClient);
    }

    /**
     * Override hashCode.
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.boardSize, this.requestingClient);
    }
}
