package com.gotp.game_mechanics.board.move;

import java.io.Serializable;

import com.gotp.game_mechanics.board.PieceType;

/**
 * Player can at any moment give up.
 */
public class MoveGiveUp implements Move, Serializable {
    /** Which player gives up? */
    private PieceType pieceType;

    /**
     * GiveUp constructor.
     * @param pieceType
     */
    public MoveGiveUp(final PieceType pieceType) {
        this.pieceType = pieceType;
    }

    /**
     * Getter for `pieceType` private variable.
     * @return PieceType
     */
    public PieceType getPieceType() {
        return pieceType;
    }
}
