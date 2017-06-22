package com.staspavlov.taggame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * GameImpl provides methods for game process.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public class GameImpl implements Game {

    /**
     * Minimal number of rows in the game area.
     */
    private static final int MIN_ROWS = 3;

    /**
     * Minimal number of columns in the game area.
     */
    private static final int MIN_COLS = 3;

    /**
     * Number of rows in the game area.
     */
    private final int rows;

    /**
     * Number of columns in the game area.
     */
    private final int cols;

    /**
     * Size of the game area.
     */
    private final int size;

    /**
     * Sequence of numbers in the game area.
     */
    private ArrayList<Integer> sequence;

    /**
     * Count of moves.
     */
    private int movesCount;

    /**
     * Created at date.
     */
    private final Date createdAt = new Date();

    /**
     * Completed at date.
     */
    private Date completedAt;

    /**
     * Creates game instance.
     * @param rows number of rows
     * @param cols number of columns
     */
    public GameImpl(int rows, int cols) {
        if (rows < MIN_ROWS || cols < MIN_COLS) {
            throw new IllegalArgumentException("Game area is too small");
        }
        this.rows = rows;
        this.cols = cols;
        this.size = rows * cols;
        this.sequence = new ArrayList<>(this.size);
        generateSequence();
    }

    /**
     * Make the finalize() method final.
     * @throws java.lang.Throwable
     */
    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Generates initial game sequence.
     */
    private void generateSequence()
    {
        for (int i = 0; i < size - 1; i++) {
            sequence.add(i, i + 1);
        }
        sequence.add(size - 1, 0);
    }

    /**
     * Returns number of rows in the game area.
     * @return number of rows
     */
    @Override
    public int getRows() {
        return rows;
    }

    /**
     * Returns number of columns in the game area.
     * @return number of columns
     */
    @Override
    public int getCols() {
        return cols;
    }

    /**
     * Returns size of the game area.
     * @return size of game area
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * Returns position (row and column) of element in the game area.
     * @param element
     * @return position of element
     */
    @Override
    public int[] getPosition(int element) {
        int index = sequence.indexOf(element);
        int row = index / cols;
        int col = index % cols;
        return new int[]{row, col};
    }

    /**
     * Make move of element.
     * @param element
     * @return true if success, false if error
     */
    @Override
    public boolean makeMove(int element) {
        // Can't move if game is completed
        if (isCompleted()) {
            return false;
        }
        // Can't move zero
        if (element == 0) {
            return false;
        }
        // Check if the move is possible
        int[] elemPos = getPosition(element);
        int[] zeroPos = getPosition(0);
        int rowsDiff = Math.abs(elemPos[0] - zeroPos[0]);
        int colsDiff = Math.abs(elemPos[1] - zeroPos[1]);
        if ((rowsDiff == 1 && colsDiff == 0) || (rowsDiff == 0 && colsDiff == 1)) {
            // Move is possible, make move
            int elemInd = sequence.indexOf(element);
            int zeroInd = sequence.indexOf(0);
            sequence.set(zeroInd, element);
            sequence.set(elemInd, 0);
            movesCount++;
            checkCompleted();
            return true;
        }
        return false;
    }

    /**
     * Checks if sequence of elements is completed.
     */
    private void checkCompleted() {
        for (int i = 0; i < size; i++) {
            int expected;
            // Last element must be zero
            if (i == size - 1) {
                expected = 0;
            // Other elements must be in ascending order
            } else {
                expected = i + 1;
            }
            if (sequence.get(i) != expected) {
                return;
            }
        }
        // Set completed at date
        completedAt = new Date();
    }

    /**
     * Checks if the game is completed.
     */
    @Override
    public boolean isCompleted() {
        return completedAt != null;
    }

    /**
     * Shuffles sequence of elements.
     */
    @Override
    public void shuffle() {
        if (!isCompleted()) {
            Collections.shuffle(sequence);
        }
    }

    /**
     * Returns count of moves.
     * @return Count of moves
     */
    @Override
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Returns count of seconds.
     * @return Count of seconds
     */
    @Override
    public int getSecondsCount() {
        Date last;
        if (isCompleted()) {
            last = completedAt;
        } else {
            last = new Date();
        }
        return (int) (last.getTime() - createdAt.getTime()) / 1000;
    }

}
