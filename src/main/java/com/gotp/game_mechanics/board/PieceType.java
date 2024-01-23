package com.gotp.game_mechanics.board;

public enum PieceType {
    /**
     * Defines piece types, also treats empty place as a piece type
     * to simplify some logic and avoid Nulls.
     */
    WHITE, BLACK, EMPTY;

    /**
     * Returns a short name of the piece type.
     * Used for printing the board.
     * @return String
     */
    public String shortName() {
        switch (this) {
            case BLACK:
                return "B";
            case WHITE:
                return "W";
            case EMPTY:
                return " ";
            default:
                return " ";
        }
    }

    /**
     * Get a pieceType from a short name.
     * @param shortName
     * @return PieceType
     */
    public static PieceType fromShortName(final String shortName) {
        switch (shortName) {
            case "B":
                return BLACK;
            case "W":
                return WHITE;
            case "E":
                return EMPTY;
            case " ":
                return EMPTY;
            default:
                return EMPTY;
        }
    }

    /**
     * Returns a long name of the piece type.
     * @return String
     */
    public String longName() {
        switch (this) {
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            case EMPTY:
                return "Empty";
            default:
                return "Empty";
        }
    }

    /**
     * Returns true if piece type is a stone.
     * @return boolean
     */
    public boolean isNotEmpty() {
        switch (this) {
            case BLACK:
                return true;
            case WHITE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns opposite piece type.
     * @return PieceType
     */
    public PieceType opposite() {
        switch (this) {
            case BLACK:
                return WHITE;
            case WHITE:
                return BLACK;
            default:
                return EMPTY;
        }
    }
}
