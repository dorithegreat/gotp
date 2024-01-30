package com.gotp.server.bot;

import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.move.Move;

public interface Bot {
    /**
     * Returns the move the bot wants to make.
     * @param gameState
     * @return Move
     */
    Move getMove(GameState gameState);
}
