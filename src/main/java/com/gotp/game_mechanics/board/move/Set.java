package com.gotp.game_mechanics.board.move;

/**
 * Represents setting a piece on the board.
 * The pieceType information is kind of redundant,
 * however I store just in case.
 */
public class Set implements Move { 
    private final Vector field;
    private final PieceType pieceType;

    /**
     * Getter for `field` private variable.
     */
    public Vector getField() {
        return field;
    }

    /**
     * Getter for `pieceType` private variable.
     */
    public PieceType getPieceType() {
        return pieceType;
    }
}