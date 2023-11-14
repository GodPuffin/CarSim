package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.util.Set;

public class Inputs {
    double throttle;
    double brake;
    double steeringAngle = 0;


    public void setBrake(double brake) {
        // Ensure that brake engagement is in the range [0, 1]
        this.brake = Math.max(0.0, Math.min(1.0, brake));
    }

    public void updateSteeringAngle(double steeringChange) {
        steeringAngle += steeringChange;
        steeringAngle = Math.min(Math.max(steeringAngle, -Math.PI / 4), Math.PI / 4);
        steeringAngle *= 0.9;
    }

    public double getBrake() {
        return brake;
    }

    public double getSteeringAngle() {
        return steeringAngle;
    }

    public double getThrottle() {
        return throttle;
    }

    public void update(Set<KeyCode> activeKeys){
        double deltaAngle = 0;

        if (activeKeys.contains(KeyCode.LEFT)) {
            deltaAngle = -0.1;
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            deltaAngle = 0.1;
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

        updateSteeringAngle(deltaAngle);
    }
}
