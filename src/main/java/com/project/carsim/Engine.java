package com.project.carsim;

public class Engine {

    double rpm;    // Current engine RPM
    double torque; // Current engine torque


    public Engine() {
        rpm = 1000.0; // Initial RPM
        torque = 0;
    }


    public void setRpm(double rpm) {
        this.rpm = Math.max(1000, rpm);
    }

    public void setTorque(double torque) {
        this.torque = torque;
    }

    double torqueCurve(double currentRPM) {
        final double[] RPM_POINTS = {1000.0, 2000.0, 3000.0, 4000.0, 4400.0, 5000.0, 5500.0, 6000.0};
        final double[] TORQUE_POINTS = {400.0, 430.0, 450.0, 465.0, 475.0, 460.0, 450.0, 390.0};


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

    public void update(double dt, double throttle) {
        setRpm(Wheel.angularSpeed);
        this.torque = throttle * torqueCurve(rpm);


    }


}
