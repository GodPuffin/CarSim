package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.text.DecimalFormat;
import java.util.Set;

public class Car {

    private static final double SPEED_THRESHOLD = 4;
    private DecimalFormat df = new DecimalFormat("#.##");

    private double enginePower;
    private double wheelbase;        // wheelbase in m
    private double b;                // in m, distance from CG to front axle
    private double c;                // in m, distance from CG to rear axle
    private double h;                // in m, height of CM from ground
    private double mass;             // in kg
    private double inertia;          // in kg.m
    private double length, width;
    private double wheellength, wheelwidth;
    private boolean sliding;

    private Vector position;         // position of car centre in world coordinates
    private Vector velocity_wc;      // velocity vector of car in world coordinates

    private double angle;            // angle of car body orientation (in rads)
    private double angularvelocity;

    private Inputs inputs;

    private Vector velocity;
    private Vector acceleration_wc;
    private double rot_angle;
    private double sideslip;
    private double slipanglefront;
    private double slipanglerear;
    private Vector force;
    private Vector resistance;
    private Vector acceleration;
    private double torque;
    private double angular_acceleration;
    private double sn, cs;
    private double yawspeed;
    private double weight;
    private Vector ftraction;
    private Vector flatf, flatr;
    private Vector fdrive;


    public Car() {

        enginePower = 10000;
        this.b = 2.0;                     // m
        this.c = 2.0;                     // m
        this.wheelbase = this.b + this.c; // m
        this.h = 1.0;                     // m
        this.mass = 1500;                 // kg
        this.inertia = 1500;              // kg.m
        this.width = 2;                 // m
        this.length = 2.75;                // m, must be > wheelbase
        this.wheellength = 0.7;
        this.wheelwidth = 0.3;

        this.position = new Vector(600, 450);
        this.velocity_wc = new Vector();

        this.angle = Math.PI / 4;
        this.angularvelocity = 0;

        inputs = new Inputs();

        velocity = new Vector();
        acceleration_wc = new Vector();
        force = new Vector();
        resistance = new Vector();
        acceleration = new Vector();
        ftraction = new Vector();
        flatf = new Vector();
        flatr = new Vector();
        fdrive = new Vector();


    }

    public void update(double dt, Set<KeyCode> activeKeys, Surface surface) {

        sn = Math.sin(this.angle);
        cs = Math.cos(this.angle);


        // transform velocity in world reference frame to velocity in car reference frame
        velocity.x = sn * this.velocity_wc.y + cs * this.velocity_wc.x;
        velocity.y = -cs * this.velocity_wc.y + sn * this.velocity_wc.x;


// Lateral force on wheels
//
        // Resulting velocity of the wheels as result of the yaw rate of the car body
        // v = yaw rate * r where r is distance of wheel to CG (approx. half wheel base)
        // yaw rate (ang.velocity) must be in rad/s
        //
        yawspeed = this.wheelbase * 0.5 * this.angularvelocity;

        double speed = velocity.magnitude();
        if (speed < SPEED_THRESHOLD) {
            // Below speed threshold, simplifying the calculations
            slipanglefront = 0;
            slipanglerear = 0;
            sliding = false;
        } else {
            // Existing calculations for slip angles
            yawspeed = this.wheelbase * 0.5 * this.angularvelocity;
            rot_angle = speed == 0 ? 0 : Math.atan2(yawspeed, speed);
            sideslip = speed == 0 ? 0 : Math.atan2(velocity.y, speed);
            slipanglefront = sideslip + rot_angle - this.inputs.getSteeringAngle();
            slipanglerear = sideslip - rot_angle;
            sliding = (Math.abs(sideslip) >= 0.7);
        }

        // weight per axle = half car mass times 1G (=9.8m/s^2)
        weight = this.mass * 9.8 * 0.5;

// lateral force on front wheels = (Ca * slip angle) capped to friction circle * load
        flatf.x = 0;
        flatf.y = Constants.CA_F * slipanglefront;
        flatf.y = Math.min(surface.getFriction(), flatf.y);
        flatf.y = Math.max(-surface.getFriction(), flatf.y);
        flatf.y *= weight;

        // lateral force on rear wheels
        flatr.x = 0;
        flatr.y = Constants.CA_R * slipanglerear;
        flatr.y = Math.min(surface.getFriction(), flatr.y);
        flatr.y = Math.max(-surface.getFriction(), flatr.y);
        flatr.y *= weight;


        if (this.inputs.getBrake() == 1 && (Math.abs(velocity.magnitude()) <= 0.1)) {
            velocity.x = 0;
            ftraction.x = 0;
            resistance.x = 0;
            resistance.y = 0;
            force.x = 0;
            force.y = 0;
        } else {
            ftraction.x = enginePower * (this.inputs.getThrottle() - this.inputs.getBrake() * Math.signum(velocity.x));
            ftraction.y = 0;

            // drag and rolling resistance
            resistance.x = -(Constants.RESISTANCE * velocity.x + Constants.DRAG * velocity.x * Math.abs(velocity.x));
            resistance.y = -(Constants.RESISTANCE * velocity.y + Constants.DRAG * velocity.y * Math.abs(velocity.y));

            // sum forces
            force.x = Double.parseDouble(df.format(ftraction.x + Math.sin(this.inputs.getSteeringAngle()) * flatf.x + flatr.x + resistance.x));
            force.y = Double.parseDouble(df.format(ftraction.y + Math.cos(this.inputs.getSteeringAngle()) * flatf.y + flatr.y + resistance.y));
        }


        // torque on body from lateral forces
        torque = speed > SPEED_THRESHOLD ? this.b * flatf.y - this.c * flatr.y : velocity.magnitude() * inputs.getSteeringAngle() * 1000;

// Acceleration

        // Newton F = m.a, therefore a = F/m
        acceleration.x = force.x / this.mass;
        acceleration.y = force.y / this.mass;
        angular_acceleration = torque / this.inertia;

// Velocity and position

        // transform acceleration from car reference frame to world reference frame
        acceleration_wc.x = sn * acceleration.y + cs * acceleration.x;
        acceleration_wc.y = -cs * acceleration.y + sn * acceleration.x;

        // velocity is integrated acceleration
        //
        this.velocity_wc.x += dt * acceleration_wc.x;
        this.velocity_wc.y += dt * acceleration_wc.y;

        // position is integrated velocity
        //
        this.position.x += dt * this.velocity_wc.x;
        this.position.y += dt * this.velocity_wc.y;

// Angular velocity and heading

        // integrate angular acceleration to get angular velocity
        //
        this.angularvelocity += dt * angular_acceleration;

        // integrate angular velocity to get angular orientation
        //
        this.angle += dt * this.angularvelocity;

        // Low speed fix
        if (speed < SPEED_THRESHOLD) {
            this.angularvelocity *= 0.9;
            this.velocity_wc.x *= 0.99;
            this.velocity_wc.y *= 0.99;
        }

        inputs.update(activeKeys);
    }

    public void reset() {

//        reset inputs
        inputs.reset();

//        resets movement
        position = new Vector(600, 450);
        velocity_wc = new Vector();
        acceleration_wc = new Vector();

//        resets rotation
        angle = Math.PI / 4;
        angularvelocity = 0;
        angular_acceleration = 0;

    }

    public void setEnginePower(double enginePower) {
        this.enginePower = enginePower;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public void setInertia(double inertia) {
        this.inertia = inertia;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getWheellength() {
        return wheellength;
    }

    public double getWheelwidth() {
        return wheelwidth;
    }

    public boolean isSliding() {
        return sliding;
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getVelocity_wc() {
        return velocity_wc;
    }

    public double getAngle() {
        return angle;
    }

    public Vector getAcceleration_wc() {
        return acceleration_wc;
    }

    public double getThrottle() {
        return this.inputs.getThrottle();
    }

    public double getBrake() {
        return this.inputs.getBrake();
    }

    public double getSteeringAngle() {
        return this.inputs.getSteeringAngle();
    }
}
