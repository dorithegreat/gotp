package com.gotp.game_mechanics.utilities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Used mostly to store pairs of numbers (x, y) in a handy way.
 * Equiped with methods that make working with pairs of numbers easier.
 */
public class Vector implements Serializable {
    /** First component of the vector. */
    private int x;

    /** Second component of the vector. */
    private int y;

    /**
     * Constructor.
     * @param x
     * @param y
     */
    public Vector(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for `this.x` .
     * @return x component of the vector.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for `this.y` .
     * @return y component of the vector.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Setter for `this.x` .
     * @param value new value for this.x
     */
    public void setX(final int value) {
        this.x = value;
    }

    /**
     * Setter for `this.y` .
     * @param value new value for this.y
     */
    public void setY(final int value) {
        this.y = value;
    }

    /**
     * Vector addition implementation.
     * @param other
     * @return vector sum of this and other.
     */
    public Vector add(final Vector other) {
        return new Vector(this.x + other.getX(), this.y + other.getY());
    }

    /**
     * Checks for equality on both components.
     * @param other
     * @return boolean
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Vector)) {
            return false;
        }

        Vector otherVector = (Vector) other; // Can cast because I returned earlier.

        return this.x == otherVector.getX() && this.y == otherVector.getY();
    }

    /**
     * Implements hash, because I overriden `equals` method.
     * @return hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    /**
     * Returns string representation of the vector.
     */
    @Override
    public String toString() {
        return "Vec(" + this.x + ", " + this.y + ")";
    }
}
