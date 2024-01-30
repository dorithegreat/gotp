package com.gotp.game_mechanics.board.move;

import java.io.Serializable;

import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

/**
 * Represents setting a piece on the board.
 * The pieceType information is kind of redundant,
 * however I store just in case.
 */
public class MovePlace implements Move, Serializable {
    /**
     * `field` is the field on which the piece is set.
     */
    private final Vector field;

    /**
     * `pieceType` is the type of the piece that is set.
     */
    private final PieceType pieceType;

    /**
     * Set constructor.
     * @param field
     * @param pieceType
     */
    public MovePlace(final Vector field, final PieceType pieceType) {
        this.field = field;
        this.pieceType = pieceType;
    }

    /**
     * Getter for `field` private variable.
     * @return Vector
     */
    public Vector getField() {
        return field;
    }

    /**
     * Getter for `pieceType` private variable.
     * @return PieceType
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Override toString() method.
     * @return String
     */
    @Override
    public String toString() {
        return "MovePlace{"
                + "field=" + field
                + ", pieceType=" + pieceType
                + '}';
    }
}
