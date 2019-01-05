package Utilities;

import simbad.sim.LineSensor;
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

    public static double luxToLuminance(double lux) {
        return Math.pow(lux, 0.1);
    }

    public static double luxToLuminance(double lLux, double rLux) {
        double lLum = luxToLuminance(lLux);
        double rLum = luxToLuminance(rLux);

        return (lLum + rLum) / 2.0;
    }

    public static int getMinSonarIndex(RangeSensorBelt sonars) {
        int min = 0;
        for (int i = 1; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        }

        return min;
    }

    public static double getMinSonar(RangeSensorBelt sonars) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < sonars.getNumSensors(); i++)
            if (sonars.getMeasurement(i) < min)
                min = sonars.getMeasurement(i);

        return min;
    }

    public static final class LineSensorHalfs {
        private int right;
        private int left;

        public LineSensorHalfs(LineSensor line) {
            calcRightAndLeftHalfs(line);
        }

        public int getRight() {
            return right;
        }

        public int getLeft() {
            return left;
        }

        private void calcRightAndLeftHalfs(LineSensor line) {
            right = 0;
            left = 0;

            for (int i = 0; i < line.getNumSensors() / 2; i++) {
                left += line.hasHit(i) ? 1 : 0;
                right += line.hasHit(line.getNumSensors() - i - 1) ? 1 : 0;
            }
        }
    }
}
