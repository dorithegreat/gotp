package com.game_mechanics_test;


import org.junit.Test;

import com.gotp.game_mechanics.board.Board;

public class BoardTest {

    /**
     * Test passes if Board(-1) throws an Exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSize() {
        new Board(-1);
    }
}
