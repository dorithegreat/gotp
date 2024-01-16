package com.game_mechanics_test;


import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

public class BoardTest {

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


        ArrayList<HashSet<Vector>> groups = (ArrayList<HashSet<Vector>>) testBoard.groups();
        for (HashSet<Vector> group : groups) {
            // System.out.println(group);
        }

        // for (Vector field : firstGroup) {
        //     System.out.println(field);
        // }

        System.out.println(testBoard);
    }
}
