package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.util.Set;

public class Car {

    public Vector directionHeading;
    public Vector position;
    public final double WHEELBASE = 40;
    public final double TRACK = 20;
    public double steeringAngle;
    private Surface surface;

    public Car() {
        this.reset(600, 450);
    }
    public void update(double deltaTime, Set<KeyCode> activeKeys, Surface surface) {
        this.surface = surface;
    }

    public void reset(double x, double y) {
        directionHeading = new Vector(1, 0);
        position = new Vector(x, y);
        steeringAngle = 0;
    }
}
