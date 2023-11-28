package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.util.Set;

/**
 * Class for handling user inputs
 * @author Philip
 */
public class Inputs {
    private double throttle;
    private double brake;
    private double steeringAngle;

    /**
     * Constructor for Inputs
     */
    Inputs() {
        this.steeringAngle = 0;
        this.throttle = 0;
        this.brake = 0;
    }

    /**
     * Sets the brake
     * @param brake Brake amount
     */
    public void setBrake(double brake) {
        // Ensure that brake engagement is in the range [0, 1]
        this.brake = Math.max(0.0, Math.min(1.0, brake));
    }

    /**
     * Updates the steering angle
     * @param steeringChange Change in steering angle
     */
    public void updateSteeringAngle(double steeringChange) {
        steeringAngle += steeringChange;
        steeringAngle = Math.min(Math.max(steeringAngle, -Math.PI / 4), Math.PI / 4);
        steeringAngle *= 0.9;
    }

    /**
     * Gets the brake
     * @return Brake amount
     */
    public double getBrake() {
        return brake;
    }

    /**
     * Gets the steering angle
     * @return Steering angle
     */
    public double getSteeringAngle() {
        return steeringAngle;
    }

    /**
     * Gets the throttle
     * @return Throttle amount
     */
    public double getThrottle() {
        return throttle;
    }

    /**
     * Updates the inputs
     * @param activeKeys Set of active keys
     */
    public void update(Set<KeyCode> activeKeys) {
        double deltaAngle = 0;
        brake = 0;
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
            throttle = (throttle > 0.05) ? throttle - 0.05 : 0;
            brake = 1;

        } else {
            throttle = (throttle > 0.05) ? throttle * 0.95 : 0;
        }
        updateSteeringAngle(deltaAngle);
    }

    /**
     * Resets the inputs
     */
    public void reset() {
        this.throttle = 0;
        this.brake = 0;
        this.steeringAngle = 0;
    }
}
