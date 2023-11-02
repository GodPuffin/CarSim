package com.project.carsim;

public class Engine {
    private double engineRPM;    // Current engine RPM
    private double torque; // Current engine torque
    private double throttlePosition; // Throttle position (0 to 1)

    public Engine() {
        engineRPM = 1000.0; // Initial RPM
        torque = torqueCurve(engineRPM);
        throttlePosition = 0.0; // Throttle fully released
    }


    // Ensure that throttle position is in the range [0, 1]
    public void setThrottlePosition(double position) {
        throttlePosition = Math.max(0.0, Math.min(1.0, position));
    }

    private double torqueCurve(double currentRPM) {
        // Torque curve for engine LS1 in SI units
        final double[] RPM_POINTS =    {1000.0, 2000.0, 3000.0, 4000.0, 4400.0, 5000.0, 5500.0, 6000.0};
        final double[] TORQUE_POINTS = { 400.0,  430.0,  450.0,  465.0,  475.0,  460.0,  450.0,  390.0};


        // Using interpolation to estimate torque based on RPM
        for (int i = 1; i < RPM_POINTS.length; i++) {
            if (currentRPM <= RPM_POINTS[i]) {
                double x0 = RPM_POINTS[i - 1];
                double x1 = RPM_POINTS[i];
                double y0 = TORQUE_POINTS[i - 1];
                double y1 = TORQUE_POINTS[i];

                return y0 + (currentRPM - x0) * (y1 - y0) / (x1 - x0);
            }
        }
        return TORQUE_POINTS[TORQUE_POINTS.length - 1];
    }

    public void update(double dt){

    }

}
