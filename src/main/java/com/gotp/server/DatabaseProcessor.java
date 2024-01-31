package com.gotp.server;

import java.io.IOException;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.move.Move;

public class DatabaseProcessor {

    /**
     * GameHistory object containing all the moves of the previous game.
     */
    private GameHistory history;

    /**
     * reference to the BoardCommunicator.
     */
    private BoardCommunicator board = BoardCommunicator.getInstance();

    /**
     * constructor. sets history to the received history of the last game.
     */
    public DatabaseProcessor(GameHistory history){
        this.history = history;
    }

    /**
     * starts replaying the game. Makes moves sequentially and waits a second between them.
     * @throws IOException
     * @throws InterruptedException
     */
    public void startReplay() throws IOException, InterruptedException{
        for (Move move : history) {
            board.makeMove(move);
            wait(1000);
        }
    }
}
