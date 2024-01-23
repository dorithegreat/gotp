package com.game_mechanics_test.utilities_test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.gotp.game_mechanics.utilities.Vector;


public class VectorTest {
    /**
     * Test passes if constructor works as expected.
     */
    @Test
    public void constructorTest() {
        final int testX = 21;
        final int testY = 37;
        final Vector testVector = new Vector(testX, testY);

        assertEquals(testVector.getX(), testX);
        assertEquals(testVector.getY(), testY);
    }

    /**
     * Test passes if getters and setters work as expected.
     */
    @Test
    public void gettersSettersTest() {
        final int testX = 21;
        final int testY = 37;
        final Vector testVector = new Vector(0, 0);

        testVector.setX(testX);
        testVector.setY(testY);

        assertEquals(testVector.getX(), testX);
        assertEquals(testVector.getY(), testY);
    }

    /**
     * Test passes if vector addition works as expected.
     */
    @Test
    public void additionTest() {
        final int testX1 = 21;
        final int testY1 = 37;
        final int testX2 = 42;
        final int testY2 = 73;
        final Vector testVector1 = new Vector(testX1, testY1);
        final Vector testVector2 = new Vector(testX2, testY2);

        final Vector sumVector = testVector1.add(testVector2);

        assertEquals(sumVector.getX(), testX1 + testX2);
        assertEquals(sumVector.getY(), testY1 + testY2);
    }

    /**
     * Test passes if vector equals() works as expected.
     */
    @Test
    public void equalsTest() {
        final int testX = 21;
        final int testY = 37;
        final Vector testVector1 = new Vector(testX, testY);
        final Vector testVector2 = new Vector(0, 0);

        testVector2.setX(testX);
        testVector2.setY(testY);

        assertNotSame(testVector1, testVector2);
        assertEquals(testVector1, testVector2);
    }

    /**
     * Test passes if vector toString() works as expected.
     */
    @Test
    public void toStringTest() {
        final int testX = 21;
        final int testY = 37;
        final Vector testVector = new Vector(testX, testY);

        assertEquals(testVector.toString(), "Vec(" + testX + ", " + testY + ")");
    }
}

