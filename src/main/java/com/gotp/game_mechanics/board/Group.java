package com.gotp.game_mechanics.board;

import java.util.HashSet;

import com.gotp.game_mechanics.utilities.Vector;

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
     * @return String
     */
    @Override
    public String toString() {
        return "Group<" + this.pieceType.longName() + "> {" + super.toString().replace("[", "").replace("]", "") + "}";
    }

    /**
     * Overrides equals() method.
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Group) {
            Group other = (Group) obj;
            return this.pieceType == other.pieceType && super.equals(other);
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

