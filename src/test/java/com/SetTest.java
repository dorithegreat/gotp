package com;


import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import com.gotp.game_mechanics.utilities.Vector;


public class SetTest {
    /**
     * Test passes if vectors with the same entries are equal.
     */
    @Test
    public void testEqual() {
        final int testValue1 = 6;
        final int testValue2 = 9;

        final Vector vec1 = new Vector(testValue1, testValue2);
        final Vector vec2 = new Vector(testValue1, testValue2);

        assertEquals(vec1, vec2);
    }

    /**
     * Test passes if HashSet recognizes identical vectors and stores only one.
     */
    @Test
    public void testHashSet() {
        final int testValue1 = 6;
        final int testValue2 = 9;
        final int testValue3 = 21;
        final int testValue4 = 37;

        final Vector vec1 = new Vector(testValue1, testValue2);
        final Vector vec2 = new Vector(testValue1, testValue2);
        final Vector vec3 = new Vector(testValue3, testValue4);

        HashSet<Vector> set = new HashSet<Vector>();
        set.add(vec1);
        set.add(vec2);
        set.add(vec3);

        assertEquals(2, set.size());
    }
}
