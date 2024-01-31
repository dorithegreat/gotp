package com.gotp.database;

import java.util.ArrayList;
import java.util.List;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.MoveGiveUp;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class DatabaseMock implements Database {
    /**
     * Insert a game history into the database.
     * @param gameHistory
     */
    @Override
    public void insertGameHistory(final GameHistory gameHistory) {
        return;
    }

    /**
     * Get a game history from the database.
     * @return GameHistory
     */
    @Override
    public GameHistory getGameHistory(final int id) {
        return getMockGameHistory();
    }

    /**
     * Get all Game Histories from the database.
     * @return List<GameHistory>
     */
    @Override
    public List<GameHistory> getAllGameHistories() {
        return new ArrayList<GameHistory>();
    }

    /**
     * Get the last game history from the database.
     * @return GameHistory
     */
    @Override
    public GameHistory getLastGameHistory() {
        return getMockGameHistory();
    }

    private GameHistory getMockGameHistory() {
        final int boardSize = 7;
        GameState gameState = new GameState(boardSize);
        gameState.makeMove(new MovePlace(new Vector(0, 0), PieceType.BLACK));
        gameState.makeMove(new MovePlace(new Vector(1, 0), PieceType.WHITE));

        gameState.makeMove(new MovePlace(new Vector(2, 0), PieceType.BLACK));
        gameState.makeMove(new MovePlace(new Vector(3, 0), PieceType.WHITE));

        gameState.makeMove(new MovePass(PieceType.BLACK));
        gameState.makeMove(new MovePlace(new Vector(4, 0), PieceType.WHITE));

        gameState.makeMove(new MoveGiveUp(PieceType.BLACK));

        return gameState.getHistory();
    }
}
