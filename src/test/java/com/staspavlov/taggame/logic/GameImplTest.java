package com.staspavlov.taggame.logic;

import com.staspavlov.taggame.game.GameImpl;
import com.staspavlov.taggame.game.Game;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameImplTest {

    @Test
    public void testInit() {
        Game g = new GameImpl(3, 4);
        assertEquals(3, g.getRows());
        assertEquals(4, g.getCols());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWrongRows() {
        Game g = new GameImpl(2, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWrongCols() {
        Game g = new GameImpl(3, 2);
    }

    @Test
    public void testGetNumber() {
        Game g = new GameImpl(3, 4);
        assertArrayEquals(new int[]{0, 0}, g.getPosition(1));
        assertArrayEquals(new int[]{0, 1}, g.getPosition(2));
        assertArrayEquals(new int[]{0, 2}, g.getPosition(3));
        assertArrayEquals(new int[]{0, 3}, g.getPosition(4));
        assertArrayEquals(new int[]{1, 0}, g.getPosition(5));
        assertArrayEquals(new int[]{1, 1}, g.getPosition(6));
        assertArrayEquals(new int[]{1, 2}, g.getPosition(7));
        assertArrayEquals(new int[]{1, 3}, g.getPosition(8));
        assertArrayEquals(new int[]{2, 0}, g.getPosition(9));
        assertArrayEquals(new int[]{2, 1}, g.getPosition(10));
        assertArrayEquals(new int[]{2, 2}, g.getPosition(11));
        assertArrayEquals(new int[]{2, 3}, g.getPosition(0));
    }

    @Test
    public void testIsCompleted() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Game g = new GameImpl(3, 4);
        assertFalse(g.isCompleted());

        Method method = g.getClass().getDeclaredMethod("checkCompleted");
        method.setAccessible(true);
        method.invoke(g);
        assertTrue(g.isCompleted());
    }

    @Test
    public void testMakeMove() {
        Game g = new GameImpl(3, 3);

        int[][] seq = new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        };
        assertTrue(checkSequence(g, seq));

        // Can't move zero
        assertFalse(g.makeMove(0));
        assertTrue(checkSequence(g, seq));

        // Can't move elements far from zero
        assertFalse(g.makeMove(3));
        assertTrue(checkSequence(g, seq));

        // Can't move diagonally
        assertFalse(g.makeMove(5));
        assertTrue(checkSequence(g, seq));

        // Correct move
        assertTrue(g.makeMove(6));
        seq = new int[][]{
            {1, 2, 3},
            {4, 5, 0},
            {7, 8, 6}
        };
        assertTrue(checkSequence(g, seq));

        // Correct move
        assertTrue(g.makeMove(5));
        seq = new int[][]{
            {1, 2, 3},
            {4, 0, 5},
            {7, 8, 6}
        };
        assertTrue(checkSequence(g, seq));
    }

    private boolean checkSequence(Game game, int[][] seq) {
        for (int r = 0; r < seq.length; r++) {
            for (int c = 0; c < seq[r].length; c++) {
                int[] exectedPos = {r, c};
                int[] gamePos = game.getPosition(seq[r][c]);
                if (!Arrays.equals(exectedPos, gamePos)) {
                    return false;
                }
            }
        }

        return true;
    }
}
