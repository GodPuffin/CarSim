package com.project.carsim;

public class Wheel {

    double radius;
    double width;
    double mass;
    double angularRotation;
    double angularSpeed;
    double angularAcceleration;
    double inertia;

    public Wheel() {
        this.radius = 0.3; //m
        this.mass = 30; //kg
        this.width = 0.1; //m
        this.inertia = mass * radius * radius / 2; // kg.m

        this.angularAcceleration = 0; //rad/s^2
        this.angularSpeed = 0; //rad/s
        this.angularRotation = 0; //rad
    }

    public void reset() {
        this.angularRotation = 0;
        this.angularSpeed = 0;
        this.angularAcceleration = 0;

    }

    public void update(double torque, double diffRatio, double gearRatio, double dt) {

        this.angularAcceleration = torque / this.inertia;
        this.angularSpeed = Math.max(this.angularSpeed + this.angularAcceleration * dt, diffRatio * gearRatio * 6000 * 2 * Math.PI / 60);
        this.angularRotation += this.angularSpeed * dt;

    }


}
