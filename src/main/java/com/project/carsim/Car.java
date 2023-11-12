package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.util.Set;

public class Car {

    public Vector directionHeading;
    public Vector position;
    public final double WHEELBASE = 4;
    public final double TRACK = 2;
    public final double WHEELWIDTH = 0.5;
    public final double WHEELDIAMETER = 0.7;
    public double steeringAngle;
    private Surface surface;

    public Car() {
        this.reset(60, 45);
    }
    public void update(double deltaTime, Set<KeyCode> activeKeys, Surface surface) {
        this.surface = surface;
        if (activeKeys.contains(KeyCode.LEFT)) {
            steeringAngle = -0.5;
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            steeringAngle = 0.5;
        }

    }

    public void reset(double x, double y) {
        directionHeading = new Vector(1, 0);
        position = new Vector(x, y);
        steeringAngle = 0;
    }
}
