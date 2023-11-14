package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.util.Set;

public class Car {

    public final double WHEELBASE = 4;
    public final double TRACK = 2;
    public final double WHEELWIDTH = 0.5;
    public final double WHEELDIAMETER = 0.7;
    public Vector directionHeading;
    public Vector position;
    public double deltaSteeringAngle;
    public double steeringAngle;
    public double throttle;
    private Surface surface;

    public Car() {
        this.reset(60, 45);
    }

    public void update(double deltaTime, Set<KeyCode> activeKeys, Surface surface) {
        this.surface = surface;

        // Update Keys
        if (activeKeys.contains(KeyCode.LEFT)) {
            deltaSteeringAngle = -0.07;
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            deltaSteeringAngle = 0.07;
        }
        if (activeKeys.contains(KeyCode.UP)) {
            if (throttle < 1) {
                throttle += 0.05;
            }
        } else if (activeKeys.contains(KeyCode.DOWN)) {
            if (throttle > 0.05) {
                throttle -= 0.05;
            }
        } else {
            throttle *= 0.95;
        }

        // Update steering angle
        steeringAngle += deltaSteeringAngle;
        steeringAngle *= 0.9;
        deltaSteeringAngle = 0;

        // Temporary Drive code
        directionHeading.rotate(steeringAngle * throttle * 0.2);
        position.x += directionHeading.x * throttle;
        position.y += directionHeading.y * throttle;

//        System.out.println("position: " + position.x + ", " + position.y);

    }

    public void reset(double x, double y) {
        directionHeading = new Vector(1, 0);
        position = new Vector(x, y);
        steeringAngle = 0;
    }
}
