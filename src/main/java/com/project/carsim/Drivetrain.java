package com.project.carsim;

public class Drivetrain {

    //  TRANS AND DIFF
    double[] transmissionRatio;
    int currentGear;
    double differentialRatio;
    double transmissionEfficiency; // percentage (0 to 1)
    double driveshaftRPM; //RPM of driveshaft

    //    C5 corvette gear ratios
    public void Drivetrain(){
        currentGear = 0;
        transmissionRatio = new double[]{-2.90, 0.0, 2.66, 1.78, 1.30, 1.0, 0.74, 0.5}; // R, N, 1, 2, 3, ...
        differentialRatio = 3.42;
        transmissionEfficiency = 0.9;
        driveshaftRPM = 0.0;
    }



}