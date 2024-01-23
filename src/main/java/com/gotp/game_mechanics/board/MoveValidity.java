package com.gotp.game_mechanics.board;

/**
 * * If all you need is to know whether a move is legal or not, use isLegal() method.
 * Used in GameState.isMoveLegal() to return the validity of a move.
 * It could be useful in GUI to notify player why a move is illegal.
 */
public enum MoveValidity {
    /** Legal move. */
    LEGAL("Move is legal."),

    /** Returned when trying to place PieceType.EMPTY. */
    EMPTY_PIECE("Piece you place has to be either black or white."),

    /** Returned when trying to place on wrong turn. */
    WRONG_TURN("It's not your turn to place a piece."),

    /** Returned when trying to place a piece on a field that is already occupied. */
    OUTSIDE_BOARD("You can't place a piece outside of the board."),

    /** Returned when trying to place a piece on a field that is already occupied. */
    FIELD_OCCUPIED("Field, you're trying to place a piece on, is already occupied."),

    /** Returned when a move is a suicide move. */
    SUICIDE("This is a suicide move. After this move, piece will have no liberties."),

    /** Returned when a move violates Ko Rule (Repetition). */
    REPETITION("This move violates \"Ko\" Rule. You can't repeat a position");

    /**
     * Message that could be displayed to the player.
     */
    private String message;

    MoveValidity(final String message) {
        this.message = message;
    }

    /**
     * True if a move is legal.
     * @return boolean
     */
    public boolean isLegal() {
        return this == LEGAL;
    }

    /**
     * Getter for message.
     * @return String
     */
    public String getMessage() {
        return this.message;
    }
}
