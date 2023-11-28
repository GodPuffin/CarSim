package com.project.carsim;

/**
 * Vector class for 2D vectors
 * @author Chris
 */
public class Vector {

    double x;
    double y;

    /**
     * Constructor for Vector
     * @param x x
     * @param y y
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for Vector
     */
    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Adds vectors
     * @param args Vectors to add
     * @return Sum of vectors
     */
    public static Vector add(Vector... args) {
        double x = 0;
        double y = 0;

        for (var i : args) {
            x += i.x;
            y += i.y;
        }
        return new Vector(x, y);
    }

    /**
     * Subtracts vectors
     * @param v1 Vector to subtract from
     * @param v2 Vector to subtract
     * @return Difference of vectors
     */
    public static Vector sub(Vector v1, Vector v2) {
        return new Vector(v1.x - v2.x, v1.y - v2.y);
    }

    /**
     * Multiplies vector by scalar
     * @param a Scalar
     * @param v1 Vector
     * @return Product of vector and scalar
     */
    public static Vector multiply(double a, Vector v1) {
        double x = a * v1.x;
        double y = a * v1.y;
        return new Vector(x, y);
    }

    /**
     * Divides vector by scalar
     * @param v1 Vector
     * @param a Scalar
     * @return Quotient of vector and scalar
     */
    public static Vector division(Vector v1, double a) {
        return new Vector((v1.x / a), (v1.y / a));
    }

    /**
     * Calculates dot product of vectors
     * @param v1 Vector
     * @param v2 Vector
     * @return Dot product of vectors
     */
    public static double dotProduct(Vector v1, Vector v2) {
        return (v1.x * v2.x) + (v1.y * v2.y);
    }

    /**
     * Creates a unit vector from an angle
     * @param Angle Angle
     * @return Unit vector
     */
    public static Vector fromAngle(double Angle) {
        return new Vector(Math.cos(Angle), Math.sin(Angle));
    }

    /**
     * Rotates vector
     * @param angle Angle to rotate by
     */
    public void rotate(double angle) {
        double newX = x * Math.cos(angle) - y * Math.sin(angle);
        double newY = x * Math.sin(angle) + y * Math.cos(angle);
        x = newX;
        y = newY;
    }

    /**
     * Calculates magnitude of vector
     * @return Magnitude of vector
     */
    public double magnitude() {
        return Math.sqrt((this.x * this.x) + (this.y * this.y));
    }

    /**
     * Multiplies vector by scalar
     * @param scalar Scalar
     * @return Product of vector and scalar
     */
    public Vector multiply(double scalar) {
        return new Vector(this.x * scalar, this.y * scalar);
    }

    /**
     * Converts vector to unit vector
     * @param v Vector
     * @return Unit vector
     */
    static public Vector unitVector(Vector v){
        return new Vector(v.x/ v.magnitude(),v.y/ v.magnitude());
    }

    /**
     * Converts vector to string
     * @return String representation of vector
     */
    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

