package com.game_mechanics_test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.Group;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

public class BoardTest {
    /**
     * Test passes if board built manually is equal to board built with a String constructor.
     */
    @Test
    public void stringConstructorTest() {
        final int testBoardSize = 5;

        Board testBoard = new Board(testBoardSize);

        final int bx1 = 0;
        final int by1 = 0;
        final int bx2 = 1;
        final int by2 = 0;
        final int bx3 = 0;
        final int by3 = 1;
        final int bx4 = 1;
        final int by4 = 1;
        final int bx5 = 2;
        final int by5 = 2;

        testBoard.setFields(PieceType.BLACK,
            new Vector(bx1, by1),
            new Vector(bx2, by2),
            new Vector(bx3, by3),
            new Vector(bx4, by4),
            new Vector(bx5, by5)
        );

        final int x1 = 3;
        final int y1 = 0;
        final int x2 = 3;
        final int y2 = 1;
        final int x3 = 4;
        final int y3 = 1;
        final int x4 = 2;
        final int y4 = 3;
        final int x5 = 4;
        final int y5 = 2;
        final int x6 = 4;
        final int y6 = 3;
        final int x7 = 3;
        final int y7 = 4;


        testBoard.setFields(PieceType.WHITE,
            new Vector(x1, y1),
            new Vector(x2, y2),
            new Vector(x3, y3),
            new Vector(x4, y4),
            new Vector(x5, y5),
            new Vector(x6, y6),
            new Vector(x7, y7)
        );

        Board stringBoard = new Board("BBEWE;BBEWW;EEBEW;EEWEW;EEEWE");

        assertEquals(testBoard, stringBoard);
    }

    /**
     * Test passes if Board(-1) throws an Exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSize() {
        new Board(-1);
    }

    /**
     * Test for checking group() method.
     * TODO: finish this test.
     */
    @Test
    public void groupFindTest() {
        Board testBoard = new Board("BBEWE;BBEWW;EEBEW;EEWEW;EEEWE");


        ArrayList<Group> groups = (ArrayList<Group>) testBoard.groups();
        // for (Group group : groups) {
        //     System.out.println(group);
        // }

        Group firstGroup = new Group(PieceType.BLACK);
        firstGroup.add(new Vector(1, 0));
        firstGroup.add(new Vector(0, 0));
        firstGroup.add(new Vector(1, 1));
        firstGroup.add(new Vector(0, 1));

        Group secondGroup = new Group(PieceType.BLACK);
        secondGroup.add(new Vector(2, 2));

        Group thirdGroup = new Group(PieceType.WHITE);
        thirdGroup.add(new Vector(2, 3));

        Group fourthGroup = new Group(PieceType.WHITE);
        fourthGroup.add(new Vector(4, 3));
        fourthGroup.add(new Vector(3, 0));
        fourthGroup.add(new Vector(4, 1));
        fourthGroup.add(new Vector(3, 1));
        fourthGroup.add(new Vector(4, 2));

        Group fifthGroup = new Group(PieceType.WHITE);
        fifthGroup.add(new Vector(3, 4));

        ArrayList<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);
        expectedGroups.add(thirdGroup);
        expectedGroups.add(fourthGroup);
        expectedGroups.add(fifthGroup);

        assertEquals(expectedGroups, groups);
        // System.out.println(testBoard);
    }

    /**
     * Tests the GameState.isLegalMove() method.
     * TODO: finish this test.
     */
    @Test
    public void legalMoveTest() {
        Board testBoard = new Board("BBEWE;BBEWW;EEBEW;EEWEW;EEEWE");
        GameState gameState = new GameState(testBoard);


        // False if piece type is EMPTY.
        assertEquals(
            false,
            gameState.isLegalMove(PieceType.EMPTY, new Vector(0, 0))
        );

        // False if field is outside the board.
        assertEquals(
            false,
            gameState.isLegalMove(PieceType.BLACK, new Vector(-1, 0))
        );

        // False if field is occupied.
        assertEquals(
            false,
            gameState.isLegalMove(PieceType.BLACK, new Vector(0, 0))
        );

        // assertEquals(
        //     true,
        //     gameState.isLegalMove(PieceType.BLACK, new Vector(2, 0))
        // );

        // assertEquals(
        //     true,
        //     gameState.isLegalMove(PieceType.WHITE, new Vector(2, 0))
        // );

        // assertEquals(
        //     false,
        //     gameState.isLegalMove(PieceType.BLACK, new Vector(0, 0))
        // );
    }
}
