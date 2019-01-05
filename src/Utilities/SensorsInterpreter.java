package Utilities;

import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;

public class SensorsInterpreter {
    public static Point3d getSensedPoint(float robotRadius, RangeSensorBelt sonars, int sonar) {
        double v;
        if (sonars.hasHit(sonar))
            v = robotRadius + sonars.getMeasurement(sonar);
        else
            v = robotRadius + sonars.getMaxRange();
        double x = v * Math.cos(sonars.getSensorAngle(sonar));
        double z = v * Math.sin(sonars.getSensorAngle(sonar));
        return new Point3d(x, 0, z);
    }

    public static double wrapToPi(double a) {
        if (a > Math.PI)
            return a - Math.PI * 2;
        if (a <= -Math.PI)
            return a + Math.PI * 2;
        return a;
    }

    public static double luxToLuminance(double lux) {
        return Math.pow(lux, 0.1);
    }

    public static double luxToLuminance(double lLux, double rLux) {
        double lLum = luxToLuminance(lLux);
        double rLum = luxToLuminance(rLux);

        return (lLum + rLum) / 2.0;
    }
}
