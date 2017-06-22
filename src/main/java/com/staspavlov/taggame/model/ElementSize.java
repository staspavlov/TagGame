package com.staspavlov.taggame.model;

/**
 * Element size.
 * @author Stanislav Pavlov <mail@staspavlov.com>
 */
public enum ElementSize {

    SMALL (75, 75),
    NORMAL (100, 100),
    LARGE (150, 150);

    private final double width;
    private final double height;

    ElementSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

}
