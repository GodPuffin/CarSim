package com.project.carsim;

import javafx.scene.input.KeyCode;

import java.text.DecimalFormat;
import java.util.Set;

public class Car {

    DecimalFormat df = new DecimalFormat("#.##");

    double enginePower;
    double wheelbase;        // wheelbase in m
    double b;                // in m, distance from CG to front axle
    double c;                // in m, distance from CG to rear axle
    double h;                // in m, height of CM from ground
    double mass;            // in kg
    double inertia;        // in kg.m
    double length, width;
    double wheellength, wheelwidth;
    boolean sliding;

    Vector position;        // position of car centre in world coordinates
    Vector velocity_wc;        // velocity vector of car in world coordinates

    double angle;                // angle of car body orientation (in rads)
    double angularvelocity;

    Inputs inputs;
    Wheel wheels;

    Vector velocity;
    Vector acceleration_wc;
    double rot_angle;
    double sideslip;
    double slipanglefront;
    double slipanglerear;
    Vector force;
    Vector resistance;
    Vector acceleration;
    double torque;
    double angular_acceleration;
    double sn, cs;
    double yawspeed;
    double weight;
    Vector ftraction;
    Vector flatf, flatr;
    Vector fdrive;


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

        this.position = new Vector();
        this.velocity_wc = new Vector();

        this.angle = 0;
        this.angularvelocity = 0;

        inputs = new Inputs();
        wheels = new Wheel();

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

        if( velocity.x == 0 )
            rot_angle = 0;
        else
            rot_angle = Math.atan2( yawspeed, velocity.x);
        // Calculate the side slip angle of the car (a.k.a. beta)
        if( velocity.x == 0 )
            sideslip = 0;
        else
            sideslip = Math.atan2( velocity.y, velocity.x);

        // Calculate slip angles for front and rear wheels (a.k.a. alpha)
        slipanglefront = sideslip + rot_angle - this.inputs.steeringAngle;
        slipanglerear = sideslip - rot_angle;

        sliding = (Math.abs(sideslip) >= 0.7);

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

        ftraction.x = 10000*(this.inputs.throttle - this.inputs.brake*Math.signum(velocity.x));
        ftraction.y = 0;

// Forces and torque on body

        // drag and rolling resistance
        resistance.x = -(Constants.RESISTANCE * velocity.x + Constants.DRAG * velocity.x * Math.abs(velocity.x));
        resistance.y = -(Constants.RESISTANCE * velocity.y + Constants.DRAG * velocity.y * Math.abs(velocity.y));

        // sum forces
        force.x = ftraction.x + Math.sin(this.inputs.steeringAngle) * flatf.x + flatr.x + resistance.x;
        force.y = ftraction.y + Math.cos(this.inputs.steeringAngle) * flatf.y + flatr.y + resistance.y;

        // torque on body from lateral forces
        torque = this.b * flatf.y - this.c * flatr.y;

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


        inputs.update(activeKeys);
    }

    public void reset() {

//        reset inputs
        inputs.reset();

//        resets movement
        position = new Vector();
        velocity_wc = new Vector();
        acceleration_wc = new Vector();

//        resets rotation
        angle = 0;
        angularvelocity = 0;
        angular_acceleration = 0;

    }
}
