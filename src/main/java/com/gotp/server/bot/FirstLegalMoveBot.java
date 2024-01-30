package com.gotp.server.bot;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class FirstLegalMoveBot implements Bot {
    /**
     * Returns the move the bot wants to make.
     */
    @Override
    public Move getMove(final GameState gameState) {
        PieceType pieceType = gameState.getTurn();

        // return MovePass with 10% chance.
        final double passProbability = 0.1;
        if (Math.random() < passProbability) {
            return new MovePass(pieceType);
        }

        // Othrewise return the first legal move.
        for (int i = 0; i < gameState.getBoardSize(); i++) {
            for (int ii = 0; ii < gameState.getBoardSize(); ii++) {
                Vector field = new Vector(i, ii);
                Move move = new MovePlace(field, pieceType);
                if (gameState.isLegalMove(pieceType, field).isLegal()) {
                    return move;
                }
            }
        }

        return new MovePass(pieceType);
    }
}
