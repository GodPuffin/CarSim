package com.project.carsim;

// 3 surfaces, each with different friction, and different appearance
// Asphalt, Gravel, Ice
public enum Surface {
    ASPHALT(0.8, "Asphalt"), GRAVEL(0.5, "Gravel"), ICE(0.2, "Ice");

    private final double friction;
    private final String name;

    Surface(double friction, String name) {
        this.friction = friction;
        this.name = name;
    }

    public double getFriction() {
        return friction;
    }

    public String getName() {
        return name;
    }
}
