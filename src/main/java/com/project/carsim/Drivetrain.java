package com.project.carsim;

public class Drivetrain {

    //  TRANS AND DIFF
    double[] transmissionRatio = {-2.90, 0.0, 2.66, 1.78, 1.30, 1.0, 0.74, 0.5}; // R, N, 1, 2, 3, 4, 5, 6;
    double differentialRatio;
    double transmissionEfficiency; // percentage (0 to 1)
    double driveshaftRPM; //RPM of driveshaft
    double clutch; // clutch engagement percentage (0 to 1)
    double brake; // brake engagement percentage (0 to 1)

    //    C5 corvette gear ratios
    public Drivetrain(){
        differentialRatio = 3.42;
        transmissionEfficiency = 0.9;
        driveshaftRPM = 0.0;
        clutch = 0.0;
        brake = 0.0;
    }



}
