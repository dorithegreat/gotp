package com.game_mechanics_test.board_test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.not;

import org.junit.Test;

import com.gotp.game_mechanics.board.MoveValidity;


public class MoveValidityTest {
    /**
     * Test passes if MoveValidity.LEGAL is legal
     * and every other MoveValidity is not legal.
     */
    @Test
    public void isLegalTest() {
        assertTrue(MoveValidity.LEGAL.isLegal());
        assertThat(MoveValidity.EMPTY_PIECE.isLegal(), not(true));
        assertThat(MoveValidity.WRONG_TURN.isLegal(), not(true));
        assertThat(MoveValidity.OUTSIDE_BOARD.isLegal(), not(true));
        assertThat(MoveValidity.FIELD_OCCUPIED.isLegal(), not(true));
        assertThat(MoveValidity.SUICIDE.isLegal(), not(true));
        assertThat(MoveValidity.REPETITION.isLegal(), not(true));
    }
}
