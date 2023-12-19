package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.gotp.game_mechanics.utilities.Vector;

/**
 * This class is an abstraction for a 2D array of pieces. It provides methods to communicate
 * and modify the state of the game. It also checks wchich moves are legal.
 * It's initialized by initializeEmptyBoardMatrix() method.
 */
public class Board {
    /**
     * Defines how big the board is.
     * Has to be strictly greater than 1.
     */
    private int boardSize;

    /**
     * An `boardSize` x `boardSize` sized 2D array that stores information aboout pieces.
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
    public void setField(final PieceType value, final int x, final int y) {
        this.boardMatrix[x][y] = value;
    }

    /**
     * Sets the given field a specified piece type.
     * @param coordinates
     * @param value
     */
    public void setField(final PieceType value, final Vector coordinates) {
        this.boardMatrix[coordinates.getX()][coordinates.getY()] = value;
    }

    /**
     * Sets the given fields a specified piece type.
     * @param value
     * @param coordinates
     */
    public void setFields(final PieceType value, final Vector... coordinates) {
        for (Vector coordinate : coordinates) {
            this.setField(value, coordinate);
        }
    }

    /**
     * Calcluates all neighbours of the given field.
     * @param coordinates
     * @return 4, 3 or 2 neighbours of the given field.
     */
    public ArrayList<Vector> neighbours(final Vector coordinates) {
        ArrayList<Vector> result = new ArrayList<Vector>();

        // before adding the neighbour, we have to check if it's not outside of the board.
        if (coordinates.getX() > 0) {
            result.add(coordinates.add(new Vector(-1, 0)));
        }
        if (coordinates.getX() < this.boardSize - 1) {
            result.add(coordinates.add(new Vector(1, 0)));
        }
        if (coordinates.getY() > 0) {
            result.add(coordinates.add(new Vector(0, -1)));
        }
        if (coordinates.getY() < this.boardSize - 1) {
            result.add(coordinates.add(new Vector(0, 1)));
        }

        return result;
    }

    /**
     * Given a starting positions finds all pieces which are members of the same group.
     * @param startingField
     * @return group of Vectors representing fields of the group.
     */
    public HashSet<Vector> group(final Vector startingField) {
        PieceType searchedColor = this.getField(startingField);

        HashSet<Vector> result = new HashSet<Vector>();
        HashSet<Vector> fieldsToCheck = new HashSet<Vector>();
        HashSet<Vector> newFieldsToCheck = new HashSet<Vector>();
        HashSet<Vector> alreadyChecked = new HashSet<Vector>();
        fieldsToCheck.add(startingField);

        while (!fieldsToCheck.isEmpty()) {
            for (Vector field : fieldsToCheck) {
                alreadyChecked.add(field);

                if (this.getField(field) != searchedColor) {
                    continue;
                }

                result.add(field);
                newFieldsToCheck.addAll(this.neighbours(field));
            }
            newFieldsToCheck.removeAll(alreadyChecked);
            fieldsToCheck = (HashSet<Vector>) newFieldsToCheck.clone();
        }

        return result;
    }

    /**
     * Returns all groups on the board.
     * @return ArrayList of groups.
     */
    public List<HashSet<Vector>> groups() {
        ArrayList<HashSet<Vector>> result = new ArrayList<HashSet<Vector>>();
        HashSet<Vector> alreadyChecked = new HashSet<Vector>();

        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                Vector currentField = new Vector(i, ii);
                if (alreadyChecked.contains(currentField) || this.getField(currentField) == PieceType.EMPTY) {
                    continue;
                }
                HashSet<Vector> currentGroup = this.group(currentField);
                result.add(currentGroup);
                alreadyChecked.addAll(currentGroup);
            }
        }

        return result;
    }

    /**
     * Returns board as a string.
     */
    @Override
    public String toString() {
        HashMap<PieceType, String> pieceRepresentation = new HashMap<PieceType, String>();
        pieceRepresentation.put(PieceType.EMPTY, " ");
        pieceRepresentation.put(PieceType.BLACK, "B");
        pieceRepresentation.put(PieceType.WHITE, "W");

        String result = "";
        for (int i = 0; i < this.boardSize; i++) {
            result += pieceRepresentation.get(this.boardMatrix[0][i]);
            for (int ii = 1; ii < this.boardSize; ii++) {
                result += " -- " + this.boardMatrix[ii][i].shortName();
            }
            result += "\n";

            if (i == this.boardSize - 1) {
                break;
            }

            result += "|";
            for (int ii = 1; ii < this.boardSize; ii++) {
                result += "    |";
            }
            result += "\n";
        }
        return result;
    }

    // public getGroups() {

    // }

}
