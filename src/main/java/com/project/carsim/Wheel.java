package com.project.carsim;

public class Wheel {

    double radius;
    double width;
    double mass;
    double inertia;

    public Wheel() {
        this.radius = 0.3; //m
        this.mass = 30; //kg
        this.width = 0.1; //m
        this.inertia = mass * radius * radius / 2; // kg.m
    }



}
