package com.game_mechanics_test.board_test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
import com.gotp.game_mechanics.utilities.Vector;

public class GameStateTest {
    /**
     * Tests basic illegal moves.
     */
    @Test
    public void basicIllegalMovesTest() {
        Board testBoard = new Board("WBBEE;EWWEE;EEEBE;EEBEB;EEEBE");
        GameState gameState = new GameState(testBoard, PieceType.BLACK);

        // False if piece type is EMPTY.
        assertEquals(
            MoveValidity.EMPTY_PIECE,
            gameState.isLegalMove(PieceType.EMPTY, new Vector(0, 1))
        );

        // False, it's not white's turn
        assertEquals(
            MoveValidity.WRONG_TURN,
            gameState.isLegalMove(PieceType.WHITE, new Vector(0, 1))
        );

        // False if field is outside the board.
        assertEquals(
            MoveValidity.OUTSIDE_BOARD,
            gameState.isLegalMove(PieceType.BLACK, new Vector(-1, 0))
        );

        // False if field is occupied.
        assertEquals(
            MoveValidity.FIELD_OCCUPIED,
            gameState.isLegalMove(PieceType.BLACK, new Vector(0, 0))
        );

        // True if field if resulting group has at least one liberty.
        assertEquals(
            MoveValidity.LEGAL,
            gameState.isLegalMove(PieceType.BLACK, new Vector(3, 0))
        );

        // True, because resulting group has at least one liberty.
        assertEquals(
            MoveValidity.LEGAL,
            gameState.isLegalMove(PieceType.BLACK, new Vector(3, 3))
        );

        gameState.makeMove(new MovePass(PieceType.BLACK));
        // gameState = new GameState(testBoard, PieceType.WHITE);

        // False because resulting group (one piece) has no liberties.
        assertEquals(
            MoveValidity.SUICIDE,
            gameState.isLegalMove(PieceType.WHITE, new Vector(3, 3))
        );
    }

    /**
     * Evaluation of liberies by isLegalMove().
     */
    @Test
    public void evaluationOfLiberies() {
        Board testBoard = new Board("EWWWE;WBEEW;EWWWE;EEEEE;EEEEE");
        GameState gameState = new GameState(testBoard, PieceType.BLACK);

        // First move should be legal
        assertEquals(
            MoveValidity.LEGAL,
            gameState.makeMove(new MovePlace(new Vector(2, 1), PieceType.BLACK))
        );

        gameState.makeMove(new MovePass(PieceType.WHITE));

        // but this one will strangle the group.
        assertEquals(
            MoveValidity.SUICIDE,
            gameState.isLegalMove(PieceType.BLACK, new Vector(3, 1))
        );
    }

        /**
     * Tests if captures count as legal.
     */
    public void captureLegalMoveTest() {
        // If there are no possible captures, move is illegal.
        Board testBoard = new Board("EWBEE;WEWEE;EWBEE;EEEEE;EEEEE");
        GameState gameState = new GameState(testBoard, PieceType.BLACK);

        // Piece has no liberties after placement.
        assertEquals(
            MoveValidity.SUICIDE,
            gameState.isLegalMove(PieceType.BLACK, new Vector(1, 1))
        );

        // However if we put a black piece in (3, 1), the move will be able to capture
        testBoard.setField(PieceType.BLACK, new Vector(3, 1));

        // Hence it will have liberties and the move is legal.
        assertEquals(
            MoveValidity.LEGAL,
            gameState.isLegalMove(PieceType.BLACK, new Vector(1, 1))
        );
    }

    /**
     * Tests the GameState.isLegalMove() properly
     * evaluates illegality of repeating the same board (Ko rule).
     */
    @Test
    public void repetitionRuleTest() {
        Board testBoard = new Board("EWBEE;WEWBE;EWBEE;EEEEE;EEEEE");
        GameState gameState = new GameState(testBoard, PieceType.BLACK);

        // System.out.println(gameState);

        // Black captures a white piece.
        MovePlace firstCapture = new MovePlace(new Vector(1, 1), PieceType.BLACK);
        assertEquals(
            MoveValidity.LEGAL,
            gameState.makeMove(firstCapture)
        );
        // System.out.println(gameState);

        MovePlace secondCapture = new MovePlace(new Vector(2, 1), PieceType.WHITE);
        assertEquals(
            MoveValidity.REPETITION,
            gameState.makeMove(secondCapture)
        );
    }

    /**
     * Tests the GameState.makeMove() method.
     */
    @Test
    public void makeMoveTest() {

    }
}
