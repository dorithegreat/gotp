package com.gotp.server;

import java.io.IOException;

import com.gotp.game_mechanics.board.GameHistory;
import com.gotp.game_mechanics.board.move.Move;

public class DatabaseProcessor {

    private GameHistory history;

    private BoardCommunicator board = BoardCommunicator.getInstance();

    public DatabaseProcessor(GameHistory history){
        this.history = history;
    }

    public void startReplay() throws IOException, InterruptedException{
        for (Move move : history) {
            board.makeMove(move);
            wait(1000);
        }
    }
}
