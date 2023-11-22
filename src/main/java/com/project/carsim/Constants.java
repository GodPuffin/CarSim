package com.project.carsim;

public final class Constants {

    private Constants(){} // No need to instantiate the class, so we can hide its constructor


    public static final double DRAG = 0.5; // factor for air resistance (drag)
    public static final double RESISTANCE = 15; // factor for rolling resistance
    public static final double CA_R = -5.2; // cornering stiffness
    public static final double CA_F = -5; // cornering stiffness
    public static final double MAX_GRIP = 2; // maximum (normalised) friction force, =diameter of friction circle



}