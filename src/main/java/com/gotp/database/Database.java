package com.gotp.database;

import com.gotp.game_mechanics.board.GameHistory;

public interface Database {
    /**
     * Insert a game history into the database.
     * @param gameHistory
     */
    void insertGameHistory(GameHistory gameHistory);

    /**
     * Get a game history from the database.
     * @param id
     * @return GameHistory
     */
    GameHistory getGameHistory(int id);
}
