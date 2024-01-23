package com.game_mechanics_test.board_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;

import com.gotp.game_mechanics.board.Group;
import com.gotp.game_mechanics.board.PieceType;
import com.gotp.game_mechanics.utilities.Vector;

import java.util.ArrayList;

public class GroupTest {
    /**
     * Test passes if constructor works as expected.
     */
    @Test
    public void constructorTest() {
        Group testGroup = new Group(PieceType.BLACK);

        // check if the group has the correct piece type.
        assertEquals(PieceType.BLACK, testGroup.getPieceType());

        ArrayList<Vector> testList = new ArrayList<Vector>();
        testList.add(new Vector(0, 0));
        testList.add(new Vector(1, 0));
        testList.add(new Vector(0, 1));
        testList.add(new Vector(1, 1));
        testList.add(new Vector(1, 1));

        // Test constructor from iterable collection.
        testGroup = new Group(testList, PieceType.BLACK);
        final int testGroupExpecteSize = 4;

        // check if the group doesn't contain duplicates.
        assertThat(testGroup.size(), equalTo(testGroupExpecteSize));

        // check if the group contains all the vectors.
        assertTrue(testGroup.contains(new Vector(0, 0)));
        assertTrue(testGroup.contains(new Vector(1, 0)));
        assertTrue(testGroup.contains(new Vector(0, 1)));
        assertTrue(testGroup.contains(new Vector(1, 1)));
    }

    /**
     * Test passes if exception is thrown when expected.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorTestException() {
        new Group(PieceType.EMPTY);
    }

    /**
     * Test passes if equals() works as expected.
     */
    @Test
    public void equalsTest() {
        ArrayList<Vector> testList = new ArrayList<Vector>();
        testList.add(new Vector(0, 0));
        testList.add(new Vector(1, 0));
        testList.add(new Vector(0, 1));
        testList.add(new Vector(1, 1));

        Group blackGroup = new Group(testList, PieceType.BLACK);
        Group whiteGroup = new Group(testList, PieceType.WHITE);

        // Test that groups of different colors are not equal, even though it's impossible.
        assertThat(blackGroup, not(equalTo(whiteGroup)));


        Group whiteGroup2 = new Group(PieceType.WHITE);
        whiteGroup2.add(new Vector(0, 0));
        whiteGroup2.add(new Vector(1, 0));
        whiteGroup2.add(new Vector(0, 1));
        whiteGroup2.add(new Vector(1, 1));

        // Test that groups of the same color and same pieces are equal.
        assertThat(whiteGroup, equalTo(whiteGroup2));
    }

    /**
     * Test passes if toString() works as expected.
     */
    @Test
    public void toStringTest() {
        ArrayList<Vector> testList = new ArrayList<Vector>();
        testList.add(new Vector(0, 0));
        testList.add(new Vector(1, 0));
        testList.add(new Vector(0, 1));
        testList.add(new Vector(1, 1));

        Group blackGroup = new Group(testList, PieceType.BLACK);
        Group whiteGroup = new Group(testList, PieceType.WHITE);

        // check beggining of the string
        assertTrue(blackGroup.toString().startsWith("Group<Black> {"));
        assertTrue(whiteGroup.toString().startsWith("Group<White> {"));


        // we can't test the order of the vectors in the string, so we just test that the string contains all of them.
        assertTrue(blackGroup.toString().contains("Vec(0, 0)"));
        assertTrue(blackGroup.toString().contains("Vec(1, 0)"));
        assertTrue(blackGroup.toString().contains("Vec(0, 1)"));
        assertTrue(blackGroup.toString().contains("Vec(1, 1)"));
    }
}
