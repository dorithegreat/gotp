package com.gotp.game_mechanics.board;

import java.util.ArrayList;

import com.gotp.game_mechanics.utilities.Vector;

public class Board {
    /**
     * Defines how big the board is.
     * Has to be strictly greater than 1.
     */
    private int boardSize;

    /**
     * An `boardSize` x `boardSize` sized 2D array that stores information aboout pieces.
     * This class (Board) is an abstraction for this object. It provides methods to communicate
     * and modify this object. It's initialized by initializeEmptyBoardMatrix() method.
     */
    private PieceType[][] boardMatrix;

    /**
     * Board constructor.
     * @param boardSize has to be strictly positive.
     * @throws IllegalArgumentException if boardSize was 0 or negative.
     */
    public Board(final int boardSize) throws IllegalArgumentException {
        if (boardSize <= 0) {
            throw new IllegalArgumentException("boardSize has to be greater than 0!");
        }
        this.boardSize = boardSize;
        initializeEmptyBoardMatrix();
    }

    /**
     * Used at the start to initialize the array of pieces.
     */
    private void initializeEmptyBoardMatrix() {
        this.boardMatrix = new PieceType[this.boardSize][this.boardSize];
        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                this.boardMatrix[i][ii] = PieceType.EMPTY;
            }
        }
    }

    /**
     * Getter for `boardSize` private variable.
     * @return int
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Returns piece type of the given coordinates.
     * @param x
     * @param y
     * @return PieceType
     */
    public PieceType getField(final int x, final int y) {
        return this.boardMatrix[x][y];
    }

    /**
     * Returns piece type of the given vecor.
     * @param coordinates
     * @return PieceType
     */
    public PieceType getField(final Vector coordinates) {
        return this.boardMatrix[coordinates.getX()][coordinates.getY()];
    }

    /**
     * Sets the given field a specified piece type.
     * @param x
     * @param y
     * @param value
     */
    private void setField(final int x, final int y, final PieceType value) {
        this.boardMatrix[x][y] = value;
    }

    /**
     * Sets the given field a specified piece type.
     * @param coordinates
     * @param value
     */
    private void setField(final Vector coordinates, final PieceType value) {
        this.boardMatrix[coordinates.getX()][coordinates.getY()] = value;
    }

    /**
     * Calcluates all neighbours of the given field.
     * @param coordinates
     * @return 4, 3 or 2 neighbours of the given field.
     */
    private ArrayList<Vector> neighbours(final Vector coordinates) {
        ArrayList<Vector> result = new ArrayList<Vector>();

        // before adding the neighbour, we have to check if it's not outside of the board.
        if (coordinates.getX() > 0) {
            result.add(coordinates.add(new Vector(1, 0)));
        }
        if (coordinates.getX() < this.boardSize - 1) {
            result.add(coordinates.add(new Vector(-1, 0)));
        }
        if (coordinates.getY() > 0) {
            result.add(coordinates.add(new Vector(0, 1)));
        }
        if (coordinates.getY() < this.boardSize - 1) {
            result.add(coordinates.add(new Vector(0, -1)));
        }

        return result;
    }

    // public calculateGroup() {

    // }

    // public getGroups() {

    // }

}
