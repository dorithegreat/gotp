package com.gotp.game_mechanics.board.move;

import com.gotp.game_mechanics.board.PieceType;

/**
 * To be honset, I've just needed an enum
 * that would have 2 states: SET and MOVE.
 * However a MOVE enum would need to store 2 variables
 * which seems to not be possible in Java.
 */
public interface Move {
    /**
     * Who sends the move?
     * @return PieceType
    */
    PieceType getPieceType();
}
