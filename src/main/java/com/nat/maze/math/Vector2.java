package com.nat.maze.math;

/**
 * Represents a two-dimensional vector in Cartesian coordinate system.
 */
public class Vector2 {

    /**
     * The x-component of the vector.
     */
    public double x;

    /**
     * The y-component of the vector.
     */
    public double y;

    /**
     * Constructs a new Vector2 instance with x and y components initialized to 0.
     */
    public Vector2() { }

    /**
     * Constructs a new Vector2 instance with the specified x and y components.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a new Vector2 instance that is a copy of the given vector.
     *
     * @param v The vector to copy.
     */
    public Vector2(Vector2 v) {
        set(v);
    }

    /**
     * Sets the x and y components of the vector.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the components of the vector to match the given vector.
     *
     * @param v The vector to copy.
     */
    public void set(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
    }

    /**
     * Sets the components of the vector to zero.
     */
    public void setZero() {
        x = 0;
        y = 0;
    }

    /**
     * Returns an array containing the x and y components of the vector.
     *
     * @return The components of the vector as an array in the order [x, y].
     */
    public double[] getComponents() {
        return new double[]{x, y};
    }

    /**
     * Returns the length (magnitude) of the vector.
     *
     * @return The length of the vector.
     */
    public double getLength() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * Returns the squared length (magnitude) of the vector.
     * This method is faster than {@link #getLength()} as it avoids the square root operation.
     *
     * @return The squared length of the vector.
     */
    public double getLengthSq() {
        return (x * x + y * y);
    }

    /**
     * Returns the squared distance between the vector and the specified coordinates (vx, vy).
     *
     * @param vx The x-coordinate of the target point.
     * @param vy The y-coordinate of the target point.
     * @return The squared distance between the vector and the target point.
     */
    public double distanceSq(double vx, double vy) {
        vx -= x;
        vy -= y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the squared distance between the vector and the specified vector v.
     *
     * @param v The target vector.
     * @return The squared distance between the vector and the target vector.
     */
    public double distanceSq(Vector2 v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return (vx * vx + vy * vy);
    }

    /**
     * Returns the distance between the vector and the specified coordinates (vx, vy).
     *
     * @param vx The x-coordinate of the target point.
     * @param vy The y-coordinate of the target point.
     * @return The distance between the vector and the target point.
     */
    public double distance(double vx, double vy) {
        vx -= x;
        vy -= y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the distance between the vector and the specified vector v.
     *
     * @param v The target vector.
     * @return The distance between the vector and the target vector.
     */
    public double distance(Vector2 v) {
        double vx = v.x - this.x;
        double vy = v.y - this.y;
        return Math.sqrt(vx * vx + vy * vy);
    }

    /**
     * Returns the angle (in radians) between the vector and the positive x-axis.
     *
     * @return The angle between the vector and the positive x-axis.
     */
    public double getAngle() {
        return Math.atan2(y, x);
    }

    /**
     * Normalizes the vector to have a length of 1, while preserving its direction.
     * If the vector is already a zero vector, this method does nothing.
     */
    public void normalize() {
        double length = getLength();
        if (length != 0) {
            x /= length;
            y /= length;
        }
    }

    /**
     * Adds the specified vector (vx, vy) to this vector.
     *
     * @param vx The x-component of the vector to add.
     * @param vy The y-component of the vector to add.
     */
    public void add(double vx, double vy) {
        x += vx;
        y += vy;
    }

    /**
     * Adds the specified vector v to this vector.
     *
     * @param v The vector to add.
     */
    public void add(Vector2 v) {
        x += v.x;
        y += v.y;
    }

    /**
     * Subtracts the specified vector (vx, vy) from this vector.
     *
     * @param vx The x-component of the vector to subtract.
     * @param vy The y-component of the vector to subtract.
     */
    public void subtract(double vx, double vy) {
        x -= vx;
        y -= vy;
    }

    /**
     * Subtracts the specified vector v from this vector.
     *
     * @param v The vector to subtract.
     */
    public void subtract(Vector2 v) {
        x -= v.x;
        y -= v.y;
    }

    /**
     * Multiplies this vector by the specified scalar value.
     *
     * @param scalar The scalar value to multiply by.
     */
    public void multiply(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    /**
     * Divides this vector by the specified scalar value.
     * If the scalar value is zero, this method does nothing.
     *
     * @param scalar The scalar value to divide by.
     */
    public void divide(double scalar) {
        if (scalar != 0) {
            x /= scalar;
            y /= scalar;
        }
    }

    /**
     * Returns a new vector that is the sum of this vector and the specified vector (vx, vy).
     *
     * @param vx The x-component of the vector to add.
     * @param vy The y-component of the vector to add.
     * @return The resulting vector.
     */
    public Vector2 plus(double vx, double vy) {
        return new Vector2(x + vx, y + vy);
    }

    /**
     * Returns a new vector that is the sum of this vector and the specified vector v.
     *
     * @param v The vector to add.
     * @return The resulting vector.
     */
    public Vector2 plus(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    /**
     * Returns a new vector that is the difference between this vector and the specified vector (vx, vy).
     *
     * @param vx The x-component of the vector to subtract.
     * @param vy The y-component of the vector to subtract.
     * @return The resulting vector.
     */
    public Vector2 minus(double vx, double vy) {
        return new Vector2(x - vx, y - vy);
    }

    /**
     * Returns a new vector that is the difference between this vector and the specified vector v.
     *
     * @param v The vector to subtract.
     * @return The resulting vector.
     */
    public Vector2 minus(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    /**
     * Returns a new vector that is the product of this vector and the specified scalar value.
     *
     * @param scalar The scalar value to multiply by.
     * @return The resulting vector.
     */
    public Vector2 multipliedBy(double scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    /**
     * Returns a new vector that is the quotient of this vector and the specified scalar value.
     * If the scalar value is zero, this method returns a copy of this vector.
     *
     * @param scalar The scalar value to divide by.
     * @return The resulting vector.
     */
    public Vector2 dividedBy(double scalar) {
        if (scalar != 0)
            return new Vector2(x / scalar, y / scalar);
        else
            return new Vector2(this);
    }

    /**
     * Returns a string representation of the vector in the format "(x, y)".
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}