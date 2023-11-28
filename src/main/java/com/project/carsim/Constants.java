package com.project.carsim;

/**
 * Constants class for storing constants used in the simulation
 */
public final class Constants {

    private Constants(){} // No need to instantiate the class, so we can hide its constructor


    /**
     * Air Resistance
     */
    public static final double DRAG = 5; // factor for air resistance (drag)

    /**
     * Rolling Resistance
     */
    public static final double RESISTANCE = 30; // factor for rolling resistance

    /**
     * Cornering stiffness (R)
     */
    public static final double CA_R = -5; // cornering stiffness

    /**
     * Cornering stiffness (L)
     */
    public static final double CA_F = -5; // cornering stiffness



}
