package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ModernRoboticsI2cRangeSensor implements DistanceSensor {
    private double distanceMM = distanceOutOfRange;
    private static final double MIN_DISTANCE = 50; //mm
    private static final double MAX_DISTANCE = 1000; //mm
    private static final double MAX_OFFSET = 7.0 * Math.PI / 180.0;

    public synchronized double getDistance(DistanceUnit distanceUnit) {
        double result;
        if (distanceMM < MIN_DISTANCE) result = MIN_DISTANCE - 1.0;
        else if (distanceMM > MAX_DISTANCE) result = distanceOutOfRange;
        else result = distanceMM;
        switch (distanceUnit) {
            case METER:
                return result / 1000.0;
            case CM:
                return result / 10.0;
            case MM:
                return result;
            case INCH:
                return result / 25.4;
            default:
                return result;
        }
    }

    public synchronized void updateDistance(double x, double y, double headingRadians){
        final double fieldWidth = 648;
        final double halfFieldWidth = 648/2;
        final double mmPerPixel = 144.0 * 25.4 / fieldWidth;
        final double piOver2 = Math.PI / 2.0;
        double temp = headingRadians / piOver2;
        int side = (int)Math.round(temp); //-2, -1 ,0, 1, or 2 (2 and -2 both refer to the right side)
        double offset = Math.abs(headingRadians - (side * Math.PI / 2.0));
        if (offset > MAX_OFFSET) distanceMM = distanceOutOfRange;
        else switch (side){
            case 2:
            case -2:
                distanceMM = (y + halfFieldWidth) * mmPerPixel;
                break;
            case -1:
                distanceMM = (halfFieldWidth - x) * mmPerPixel;
                break;
            case 0:
                distanceMM = (halfFieldWidth - y) * mmPerPixel;
                break;
            case 1:
                distanceMM = (x + halfFieldWidth) * mmPerPixel;
                break;
        }
    }
}
