package com.staspavlov.taggame.game;

/**
 * Game provides methods for game process.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public interface Game {

    /**
     * Returns number of rows in the game area.
     * @return number of rows
     */
    public int getRows();

    /**
     * Returns number of columns in the game area.
     * @return number of columns
     */
    public int getCols();

    /**
     * Returns size of the game area.
     * @return size of game area
     */
    public int getSize();

    /**
     * Returns position (row and column) of element in the game area.
     * @param element
     * @return position of element
     */
    public int[] getPosition(int element);

    /**
     * Make move of element.
     * @param element
     * @return true if success, false if error
     */
    public boolean makeMove(int element);

    /**
     * Checks if sequence of elements if complete.
     * @return true if complete, false if not
     */
    public boolean isCompleted();

    /**
     * Shuffles sequence of elements.
     */
    public void shuffle();

    /**
     * Returns count of moves.
     * @return Count of moves
     */
    public int getMovesCount();

    /**
     * Returns count of seconds.
     * @return Count of seconds
     */
    public int getSecondsCount();

}
