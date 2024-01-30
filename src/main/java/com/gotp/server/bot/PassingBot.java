package com.gotp.server.bot;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.move.Move;
import com.gotp.game_mechanics.board.move.MovePass;

public class PassingBot implements Bot {
    /**
     * Returns the move the bot wants to make.
     */
    @Override
    public Move getMove(final GameState gameState) {
        return new MovePass(gameState.getTurn());
    }
}
