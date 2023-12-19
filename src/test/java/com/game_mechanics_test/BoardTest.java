package com.game_mechanics_test;


import java.util.HashSet;
import java.util.Random;

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
     */
    @Test
    public void groupFindTest() {
        final int testBoardSize = 5;
        // Random randGenerator = new Random();

        // HashSet<Vector> randomGroup = new HashSet<Vector>();
        // Vector startVector = new Vector(randGenerator.nextInt(testBoardSize), randGenerator.nextInt(testBoardSize));
        // for (int i = 0; i < 10; i++) {
        //     startVector.add(

        //     )
        // }
        // randGenerator.nextInt(testBoardSize);
        Board testBoard = new Board(testBoardSize);
        // testBoard.setFields(PieceType.BLACK,
        //     new Vector(0, 0),
        //     new Vector(1, 0),
        //     new Vector(2, 0),
        //     new Vector(3, 0),
        //     new Vector(4, 0),
        //     new Vector(5, 0),
        //     new Vector(5, 1),
        //     new Vector(5, 2),
        //     new Vector(0, 1),
        //     new Vector(1, 1),
        //     new Vector(2, 1)
        // );

        testBoard.setFields(PieceType.BLACK,
            new Vector(0, 0),
            new Vector(1, 0),
            new Vector(0, 1),
            new Vector(1, 1),
            new Vector(2, 2)
        );

        HashSet<Vector> firstGroup = testBoard.group(new Vector(0, 0));

        for (Vector field : firstGroup) {
            System.out.println(field);
        }

        System.out.println(testBoard);
    }
}
