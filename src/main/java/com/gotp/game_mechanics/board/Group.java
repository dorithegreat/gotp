package com.gotp.game_mechanics.board;

import java.util.HashSet;

import com.gotp.game_mechanics.utilities.Vector;

/**
 * Represents a group in Go.
 * A group is a set of stones of the same color 
 * that are connected by a chain of adjacent stones of the same color.
 */
public class Group extends HashSet<Vector> { 
    private PieceType pieceType;

    /**
     * Getter for pieceType.
     * @return pieceType
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Group constructor.
     * @param pieceType
     */
    public Group(final PieceType pieceType) {
        if (pieceType == PieceType.EMPTY) {
            throw new IllegalArgumentException("Piece type of a group cannot be empty.");
        }

        this.pieceType = pieceType;
    }

    /**
     * Group constructor from any iterable collection of vectors.
     * @param collection
     * @param pieceType
     */
    public Group(final Iterable<Vector> collection, final PieceType pieceType) {
        if (pieceType == PieceType.EMPTY) {
            throw new IllegalArgumentException("Piece type of a group cannot be empty.");
        }

        this.pieceType = pieceType;
        for (Vector vector : collection) {
            this.add(vector);
        }
    }

    /**
     * Overrides toString() method.
     * Example: "Group<Black> {Vec(0, 0), Vec(1, 0), Vec(0, 1), Vec(1, 1), Vec(2, 2)}"
     * @return String
     */
    @Override
    public String toString() {
        return "Group<" + this.pieceType.longName() + "> {" + super.toString().replace("[", "").replace("]", "") + "}";
    }

    /**
     * Overrides equals() method.
     * @param otherObject
     * @return boolean
     */
    @Override
    public boolean equals(final Object otherObject) {
        if (otherObject instanceof Group) {
            Group otherGroup = (Group) otherObject;
            return this.pieceType == otherGroup.pieceType && super.equals(otherGroup);
        }
        return false;
    }

    /**
     * Overrides hashCode() method.
     * @return int
     */
    @Override
    public int hashCode() {
        return super.hashCode() + this.pieceType.hashCode();
    }
}

