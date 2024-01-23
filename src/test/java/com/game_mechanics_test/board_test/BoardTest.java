package com.game_mechanics_test.board_test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.gotp.game_mechanics.board.Board;
import com.gotp.game_mechanics.board.GameState;
import com.gotp.game_mechanics.board.Group;
import com.gotp.game_mechanics.board.MoveValidity;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.board.move.MovePass;
import com.gotp.game_mechanics.board.move.MovePlace;
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
     * Test passes Board constructor works properly.
     */
    @Test
    public void boardConstructorTest() {
        final int testBoardSize = 5;

        Board testBoard = new Board(testBoardSize);

        // check if board size is correct
        assertEquals(testBoardSize, testBoard.getBoardSize());

        // check if every field is empty
        for (int i = 0; i < testBoardSize; i++) {
            for (int j = 0; j < testBoardSize; j++) {
                assertEquals(PieceType.EMPTY, testBoard.getField(new Vector(i, j)));
            }
        }
    }

    /**
     * Test passes if board built manually is equal to board built with a String constructor.
     */
    @Test
    public void stringConstructorTest() {
        final int testBoardSize = 5;

        Board testBoard = new Board(testBoardSize);

        final Vector b1 = new Vector(0, 0);
        final Vector b2 = new Vector(1, 0);
        final Vector b3 = new Vector(0, 1);
        final Vector b4 = new Vector(1, 1);
        final Vector b5 = new Vector(2, 2);

        testBoard.setFields(PieceType.BLACK,
            b1, b2, b3, b4, b5
        );

        final Vector w1 = new Vector(3, 0);
        final Vector w2 = new Vector(3, 1);
        final Vector w3 = new Vector(4, 1);
        final Vector w4 = new Vector(2, 3);
        final Vector w5 = new Vector(4, 2);
        final Vector w6 = new Vector(4, 3);
        final Vector w7 = new Vector(3, 4);

        testBoard.setFields(PieceType.WHITE,
            w1, w2, w3, w4, w5, w6, w7
        );

        Board stringBoard = new Board("BBEWE;BBEWW;EEBEW;EEWEW;EEEWE");

        assertEquals(testBoard, stringBoard);
    }

    /**
     * Test getters and setters.
     */
    @Test
    public void gettersAndSettersTest() {
        final int testBoardSize = 5;

        Board testBoard = new Board(testBoardSize);

        testBoard.setField(PieceType.BLACK, 1, 2);
        assertEquals(PieceType.BLACK, testBoard.getField(1, 2));
        assertEquals(PieceType.BLACK, testBoard.getField(new Vector(1, 2)));

        testBoard.setField(PieceType.BLACK, new Vector(2, 1));
        assertEquals(PieceType.BLACK, testBoard.getField(2, 1));
        assertEquals(PieceType.BLACK, testBoard.getField(new Vector(2, 1)));

        ArrayList<Vector> vectorCollection = new ArrayList<Vector>();

        // fill row 3 with black pieces
        final int thirdRow = 3;
        for (int i = 0; i < testBoardSize; i++) {
            vectorCollection.add(new Vector(i, thirdRow));
        }

        testBoard.setFields(PieceType.BLACK, vectorCollection);
        for (int i = 0; i < testBoardSize; i++) {
            assertThat(testBoard.getField(new Vector(i, thirdRow)), is(PieceType.BLACK));
        }
    }

    /**
     * Test for neighbours() method.
     */
    @Test(expected = IllegalArgumentException.class)
    public void neighboursTest() {
        final int testBoardSize = 5;

        Board testBoard = new Board(testBoardSize);

        testBoard.neighbours(new Vector(-1, 0));
    }

    /**
     * Test for group() method.
     */
    @Test
    public void groupTest() {
        Board testBoard = new Board("EEEEEE;EBBEEE;EBBEEE;EEEEEEE;EEEEEEE;EEEEEEE");

        // calculate groups with a method.
        final Vector startingField = new Vector(1, 1);
        Group calculatedGroup = testBoard.group(startingField);

        // Manually create groups we expected to get.

        Group expectedGroup = new Group(PieceType.BLACK);
        final Vector field1 = new Vector(1, 1);
        final Vector field2 = new Vector(2, 1);
        final Vector field3 = new Vector(1, 2);
        final Vector field4 = new Vector(2, 2);

        expectedGroup.add(field1);
        expectedGroup.add(field2);
        expectedGroup.add(field3);
        expectedGroup.add(field4);

        // actual test
        assertEquals(expectedGroup, calculatedGroup);
    }

    /**
     * Test for checking groups() method.
     */
    @Test
    public void groupsTest() {
        Board testBoard = new Board("BBEWE;BBEWW;EEBEW;EEWEW;EEEWE");

        // calculate groups with a method.
        List<Group> calculatedGroup = testBoard.groups();


        // Manually create groups we expected to get.

        Group firstGroup = new Group(PieceType.BLACK);
        final Vector firstGroupVector1 = new Vector(1, 0);
        final Vector firstGroupVector2 = new Vector(0, 0);
        final Vector firstGroupVector3 = new Vector(1, 1);
        final Vector firstGroupVector4 = new Vector(0, 1);
        firstGroup.add(firstGroupVector1);
        firstGroup.add(firstGroupVector2);
        firstGroup.add(firstGroupVector3);
        firstGroup.add(firstGroupVector4);


        Group secondGroup = new Group(PieceType.BLACK);
        final Vector secondGroupVector1 = new Vector(2, 2);
        secondGroup.add(secondGroupVector1);

        Group thirdGroup = new Group(PieceType.WHITE);
        final Vector thirdGroupVector1 = new Vector(2, 3);
        thirdGroup.add(thirdGroupVector1);

        Group fourthGroup = new Group(PieceType.WHITE);
        final Vector fourthGroupVector1 = new Vector(4, 3);
        final Vector fourthGroupVector2 = new Vector(3, 0);
        final Vector fourthGroupVector3 = new Vector(4, 1);
        final Vector fourthGroupVector4 = new Vector(3, 1);
        final Vector fourthGroupVector5 = new Vector(4, 2);
        fourthGroup.add(fourthGroupVector1);
        fourthGroup.add(fourthGroupVector2);
        fourthGroup.add(fourthGroupVector3);
        fourthGroup.add(fourthGroupVector4);
        fourthGroup.add(fourthGroupVector5);

        Group fifthGroup = new Group(PieceType.WHITE);
        final Vector fifthGroupVector1 = new Vector(3, 4);
        fifthGroup.add(fifthGroupVector1);

        List<Group> expectedGroups = new ArrayList<Group>();
        expectedGroups.add(firstGroup);
        expectedGroups.add(secondGroup);
        expectedGroups.add(thirdGroup);
        expectedGroups.add(fourthGroup);
        expectedGroups.add(fifthGroup);

        // actual test
        assertEquals(expectedGroups, calculatedGroup);
    }

    /**
     * Test for checking emptyFields() method.
     */
    @Test
    public void emptyFieldsTest() {
        Board testBoard = new Board("WBWBW;BEEEB;WEEEW;BEEEB;WBWBW");

        Set<Vector> calculatedEmptyFields = testBoard.emptyFields();

        final Vector emptyField1 = new Vector(1, 1);
        final Vector emptyField2 = new Vector(2, 1);
        final Vector emptyField3 = new Vector(3, 1);
        final Vector emptyField4 = new Vector(1, 2);
        final Vector emptyField5 = new Vector(2, 2);
        final Vector emptyField6 = new Vector(3, 2);
        final Vector emptyField7 = new Vector(1, 3);
        final Vector emptyField8 = new Vector(2, 3);
        final Vector emptyField9 = new Vector(3, 3);

        Set<Vector> expectedEmptyFields = Set.of(
            emptyField1, emptyField2, emptyField3,
            emptyField4, emptyField5, emptyField6,
            emptyField7, emptyField8, emptyField9
        );

        assertEquals(expectedEmptyFields, calculatedEmptyFields);
    }

    /**
     * Test for checking liberties() method.
     */
    @Test
    public void libertiesTest() {
        Board testBoard = new Board("WBW;EEE;EEE");


        final Vector checkedField = new Vector(1, 0);
        List<Vector> calculatedLiberties = testBoard.liberties(checkedField);

        List<Vector> expectedLiberties = new ArrayList<Vector>();
        final Vector libertyField = new Vector(1, 1);
        expectedLiberties.add(libertyField);

        assertEquals(expectedLiberties, calculatedLiberties);
    }

    /**
     * Test for checking groupLiberties() method.
     */
    @Test
    public void groupLibertiesTest() {
        Board testBoard = new Board(
              "WWWWW;"
            + "WBBBE;"
            + "WBEBE;"
            + "WBBBE;"
            + "WWWWW;"
        );


        final Vector checkedField = new Vector(1, 1);
        final Group checkedGroup = testBoard.group(checkedField);

        Set<Vector> calculatedLiberties = testBoard.groupLiberties(checkedGroup);

        final Vector libertyField1 = new Vector(4, 1);
        final Vector libertyField2 = new Vector(4, 2);
        final Vector libertyField3 = new Vector(4, 3);
        final Vector libertyField4 = new Vector(2, 2);
        Set<Vector> expectedLiberties = Set.of(
            libertyField1, libertyField2, libertyField3, libertyField4
        );

        assertEquals(expectedLiberties, calculatedLiberties);
    }

    /**
     * Test for standardBoardString() method.
     */
    @Test
    public void standardStringTest() {
        String testString = "WWWW;BWBW;WBWB;BBBB;";
        Board testBoard = new Board(testString);

        System.out.println(testString);
        System.out.println(testBoard.standardBoardString());

        assertEquals(testString, testBoard.standardBoardString());
    }
}
