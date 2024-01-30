package com.gotp.game_mechanics.board.move;

import java.io.Serializable;

import com.gotp.game_mechanics.board.PieceType;

/**
 * Represents a pass move.
 */
public class MovePass implements Move, Serializable {
    /** Who passes? */
    private PieceType pieceType;

    /**
     * Pass constructor.
     * @param pieceType
     */
    public MovePass(final PieceType pieceType) {
        this.pieceType = pieceType;
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
        return "MovePass{"
                + "pieceType=" + pieceType
                + '}';
    }
}
