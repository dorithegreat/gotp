package com.gotp.game_mechanics.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gotp.game_mechanics.utilities.Vector;

/**
 * This class is an abstraction for a 2D array of pieces. It provides methods to communicate
 * and modify the state of the game. It also checks wchich moves are legal.
 * It's initialized by initializeEmptyBoardMatrix() method.
 */
public class Board {
    /** Defines how big the board is. Has to be strictly greater than 1. */
    private int boardSize;

    /** An `boardSize` x `boardSize` sized 2D array that stores information aboout pieces. */
    private PieceType[][] boardMatrix;


    // -------------------- Constructors --------------------


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
     * Fills the board with EMPTY pieces.
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
     * Board constructor.
     * Creates a board from a string.
     * Example string: "EEE;EBE;EWE"
     * @param boardString
     */
    public Board(final String boardString) {
        String[] lines = boardString.split(";");
        this.boardSize = lines.length;

        initializeEmptyBoardMatrix();

        for (int i = 0; i < this.boardSize; i++) {
            String[] fields = lines[i].split("");
            for (int ii = 0; ii < this.boardSize; ii++) {
                this.boardMatrix[ii][i] = PieceType.fromShortName(fields[ii]);
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


    // -------------------- Getter and setter methods --------------------


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
     * Sets the given fields a specified piece type.
     * @param value
     * @param coordinates
     */
    public void setFields(final PieceType value, final Iterable<Vector> coordinates) {
        for (Vector coordinate : coordinates) {
            this.setField(value, coordinate);
        }
    }


    // -------------------- Group methods --------------------

    /**
     * Calcluates all neighbours of the given field.
     * @param coordinates
     * @return 4, 3 or 2 neighbours of the given field.
     */
    public Set<Vector> neighbours(final Vector coordinates) {
        if (
            coordinates.getX() < 0
            || coordinates.getX() >= this.boardSize
            || coordinates.getY() < 0
            || coordinates.getY() >= this.boardSize
        ) {
            throw new IllegalArgumentException("Coordinates are outside of the board!");
        }

        Set<Vector> result = new HashSet<Vector>();

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
    public Group group(final Vector startingField) {
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

        return new Group(result, searchedColor);
    }

    /**
     * Returns all groups on the board.
     * @return ArrayList of groups.
     */
    public List<Group> groups() {
        ArrayList<Group> result = new ArrayList<Group>();
        HashSet<Vector> alreadyChecked = new HashSet<Vector>();

        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                Vector currentField = new Vector(i, ii);
                if (alreadyChecked.contains(currentField) || this.getField(currentField) == PieceType.EMPTY) {
                    continue;
                }
                Group currentGroup = this.group(currentField);
                result.add(currentGroup);
                alreadyChecked.addAll(currentGroup);
            }
        }

        return result;
    }

    /**
     * Returns a list of empty fields.
     * @return HashSet of empty fields.
     */
    public Set<Vector> emptyFields() {

        HashSet<Vector> emptyFields = new HashSet<Vector>();

        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                Vector currentField = new Vector(i, ii);
                if (this.getField(currentField) == PieceType.EMPTY) {
                    emptyFields.add(currentField);
                }
            }
        }

        return emptyFields;
    }

    /**
     * Returns single piece's liberties.
     * @param field
     * @return List of liberties.
     */
    public List<Vector> liberties(final Vector field) {
        if (this.getField(field) == PieceType.EMPTY) {
            throw new IllegalArgumentException("Field is empty! Can't check liberties of an empty field.");
        }

        List<Vector> result = new ArrayList<Vector>();

        for (Vector neighbour : this.neighbours(field)) {
            if (this.getField(neighbour) == PieceType.EMPTY) {
                result.add(neighbour);
            }
        }

        return result;
    }

    /**
     * Returns all liberties of the given group.
     * @param group
     * @return List of liberties.
     */
    public Set<Vector> groupLiberties(final Group group) {
        Set<Vector> result = new HashSet<Vector>();

        for (Vector field : group) {
            result.addAll(this.liberties(field));
        }

        return result;
    }

    /**
     * Returns all captured groups.
     * @return List of captured groups.
     */
    public List<Group> groupsWithoutLiberties() {
        List<Group> result = new ArrayList<Group>();

        for (Group group : this.groups()) {
            if (this.groupLiberties(group).isEmpty()) {
                result.add(group);
            }
        }

        return result;
    }


    // -------------------- Overriden methods --------------------

    /**
     * Overriden equals method.
     */
    @Override
    public boolean equals(final Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Board)) {
            return false;
        }

        Board otherBoard = (Board) other;

        if (this.boardSize != otherBoard.boardSize) {
            return false;
        }

        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                if (this.boardMatrix[i][ii] != otherBoard.boardMatrix[i][ii]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Overriden hashCode method.
     */
    @Override
    public int hashCode() {
        final int prime1 = 17;
        final int prime2 = 31;

        int result = prime1;
        result = prime2 * result + this.boardSize;
        result = prime2 * result + Arrays.deepHashCode(this.boardMatrix);
        return result;
    }

    /**
     * Returns board as a string.
     */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < this.boardSize; i++) {
            result += this.boardMatrix[0][i].shortName();
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

    /**
     * Java clone() is shallow and causes me problems.
     * Use this method instead.
     * @return Board
     */
    public Board copy() {
        Board result = new Board(this.boardSize);
        for (int i = 0; i < this.boardSize; i++) {
            for (int ii = 0; ii < this.boardSize; ii++) {
                result.boardMatrix[i][ii] = this.boardMatrix[i][ii];
            }
        }
        return result;
    }
}
