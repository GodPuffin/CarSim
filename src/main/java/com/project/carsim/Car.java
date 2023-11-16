package com.project.carsim;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;

import java.util.Set;

public class Car {

    public Engine engine;
    public Drivetrain drivetrain;
    Inputs inputs;

    String currentSurface;

    Vector position;
    Vector velocity;
    Vector acceleration;
    public Vector directionFacing;

    double mass;

    public final double WHEELBASE = 4;
    public final double TRACK = 2;
    public final double WHEELWIDTH = 0.5;
    public final double WHEELDIAMETER = 0.7;

    public final double heightCG = 1;
    public final double offsetCG = 0;
    private Surface surface;


    // PHYSICS CONSTANTS
    final double DRAG = 0.5;
    final double ROLLING_RESISTANCE = 15;
    final double BRAKING_CONSTANT = 10000;

    public Car() {

        mass = 2000;

        Wheel.mu = 1;
        Wheel.radius = 0.5;
        Wheel.width = 0.3;
        Wheel.mass = 30;
        Wheel.angularAcceleration = 0;
        Wheel.angularSpeed = 0;

        drivetrain = new Drivetrain();
        engine = new Engine();
        inputs = new Inputs();


        this.reset(60, 40);
    }

    public void update(double dt, Set<KeyCode> activeKeys, Surface surface) {
        this.surface = surface;

        inputs.update(activeKeys);

        double inertia = Wheel.mass * Wheel.radius * Wheel.radius / 2;
//        double weightRear = (((WHEELBASE / 2 + offsetCG) / WHEELBASE) * mass * 9.8 + (heightCG / WHEELBASE) * mass * acceleration.magnitude());
        double weightRear = mass*9.8/2;
        double slipRatio = velocity.magnitude() != 0 ? Math.min(Wheel.angularSpeed * Wheel.radius - velocity.magnitude(), 1) / Math.abs(velocity.magnitude()) : 0;

        engine.setRpm(Wheel.angularSpeed * drivetrain.transmissionRatio[drivetrain.currentGear + 1] * drivetrain.differentialRatio * 30 / Math.PI);
        engine.torque = inputs.throttle * engine.torqueCurve(engine.rpm);

        double torqueDrive = engine.torque * drivetrain.transmissionRatio[drivetrain.currentGear + 1] * drivetrain.differentialRatio * drivetrain.transmissionEfficiency;
        double torqueTraction = (Wheel.mu * weightRear * Wheel.radius * slipRatio);
        double torqueBrake = 0;

        Wheel.angularAcceleration = (torqueDrive + torqueTraction + torqueBrake) / inertia;
        Wheel.angularSpeed += Wheel.angularAcceleration * dt;


        System.out.println("weightRear: " + weightRear + ", torqueDrive: " + torqueDrive + ", torqueTraction: " + torqueTraction);


//        forces
        Vector forceDrive = Vector.multiply(torqueDrive / Wheel.radius, Vector.unitVector(directionFacing));
        Vector forceTraction = Vector.multiply(torqueTraction / Wheel.radius, directionFacing);
        Vector forceDrag = Vector.multiply(-DRAG * velocity.magnitude(), velocity);
        Vector forceRollingResistance = Vector.multiply(-ROLLING_RESISTANCE, velocity);
        Vector forceBraking = new Vector();

        double R = WHEELBASE / Math.sin(inputs.steeringAngle);
        double omega = velocity.magnitude() / R;
        directionFacing.rotate(omega * dt);
        directionFacing = Vector.unitVector(directionFacing);


        Vector forceLongitudinal = Vector.add(forceDrive, forceTraction, forceDrag, forceRollingResistance, forceBraking);

        acceleration = Vector.division(forceLongitudinal, mass);
        velocity = Vector.add(velocity, Vector.multiply(dt, acceleration));
        position = Vector.add(position, Vector.multiply(dt, velocity));

    }

    public void reset(double x, double y) {
        directionFacing = new Vector(1, 0);
        position = new Vector(x, y);
        velocity = new Vector();
        acceleration = new Vector();
        inputs.steeringAngle = 0;
    }
}
