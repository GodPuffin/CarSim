package com.project.carsim;

/**
 * Enum for different surfaces
 * @author Marcus
 */
public enum Surface {

    // 3 surfaces, each with different friction, and different appearance
    // Asphalt, Gravel, Ice
    ASPHALT(5, "Asphalt"), GRAVEL(3, "Gravel"), ICE(1.5, "Ice");

    private final double friction;
    private final String name;

    /**
     * Constructor for Surface
     * @param friction Coefficient of friction
     * @param name Name of the surface
     */
    Surface(double friction, String name) {
        this.friction = friction;
        this.name = name;
    }

    /**
     * Gets surface's friction
     * @return Coefficient of friction
     */
    public double getFriction() {
        return friction;
    }

    /**
     * Gets surface's name
     * @return Name of the surface
     */
    public String getName() {
        return name;
    }
}
