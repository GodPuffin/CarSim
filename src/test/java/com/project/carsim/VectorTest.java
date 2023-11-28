package com.project.carsim;

import org.junit.Test;

import static org.junit.Assert.*;

public class VectorTest {

    @Test
    public void add() {
        Vector v1 = new Vector(1, 2);
        Vector v2 = new Vector(3, 4);
        Vector v3 = new Vector(5, 6);
        Vector v4 = new Vector(7, 8);
        Vector v5 = new Vector(9, 10);
        Vector v6 = new Vector(11, 12);

        Vector v7 = Vector.add(v1, v2, v3, v4, v5, v6);

        assertEquals(36, v7.x, 0.0001);
        assertEquals(42, v7.y, 0.0001);
    }

    @Test
    public void sub() {
        Vector v1 = new Vector(1, 2);
        Vector v2 = new Vector(3, 4);
        Vector v3 = Vector.sub(v2, v1);

        assertEquals(2, v3.x, 0.0001);
        assertEquals(2, v3.y, 0.0001);
    }

    @Test
    public void multiply() {
        Vector v1 = new Vector(1, 2);
        Vector v2 = Vector.multiply(2, v1);

        assertEquals(2, v2.x, 0.0001);
        assertEquals(4, v2.y, 0.0001);
    }

    @Test
    public void division() {
        Vector v1 = new Vector(1, 2);
        Vector v2 = Vector.division(v1, 2);

        assertEquals(0.5, v2.x, 0.0001);
        assertEquals(1, v2.y, 0.0001);
    }

    @Test
    public void dotProduct() {
        Vector v1 = new Vector(1, 2);
        Vector v2 = new Vector(3, 4);
        double dotProduct = Vector.dotProduct(v1, v2);

        assertEquals(11, dotProduct, 0.0001);
    }

    @Test
    public void fromAngle() {
        Vector v1 = Vector.fromAngle(0);
        assertEquals(1, v1.x, 0.0001);
        assertEquals(0, v1.y, 0.0001);
    }

    @Test
    public void rotate() {
        Vector v1 = new Vector(1, 0);
        v1.rotate(Math.PI / 2);
        assertEquals(0, v1.x, 0.0001);
        assertEquals(1, v1.y, 0.0001);
    }

    @Test
    public void magnitude() {
        Vector v1 = new Vector(3, 4);
        assertEquals(5, v1.magnitude(), 0.0001);
    }

    @Test
    public void testMultiply() {
        Vector v1 = new Vector(3, 4);
        Vector v2 = v1.multiply(2);

        assertEquals(6, v2.x, 0.0001);
        assertEquals(8, v2.y, 0.0001);
    }

    @Test
    public void unitVector() {
        Vector v1 = new Vector(3, 4);
        Vector v2 = Vector.unitVector(v1);

        assertEquals(0.6, v2.x, 0.0001);
        assertEquals(0.8, v2.y, 0.0001);
    }
}