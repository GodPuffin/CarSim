package com.project.carsim;

public enum Surface {

    // 3 surfaces, each with different friction, and different appearance
    // Asphalt, Gravel, Ice
    ASPHALT(5, "Asphalt"), GRAVEL(3, "Gravel"), ICE(1.5, "Ice");

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
