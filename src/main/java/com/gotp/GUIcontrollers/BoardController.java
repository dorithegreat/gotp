package com.gotp.GUIcontrollers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class BoardController {

    /**
     * grid that will become the board.
     */
    @FXML
    private GridPane grid;

    /**
     * generates the board.
     * @param boardSize size of the created board
     */
    @FXML
    public void addToGrid(final int boardSize) throws IOException {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                grid.add(new BoardPiece(), i, j);
                //! rows that are added beyond the initial ones are scaled differently
                //I'll fix that when I figure out what's causing it
            }
        }
    }

}
