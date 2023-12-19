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
}
