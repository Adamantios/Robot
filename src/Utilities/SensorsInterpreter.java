package Utilities;

import simbad.sim.LineSensor;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;

public class SensorsInterpreter {
    private static float robotRadius;

    public static void setRobotRadius(float robotRadius) {
        SensorsInterpreter.robotRadius = robotRadius;
    }

    /**
     * Calculates the point from a certain sonar.
     *
     * @param sonars   the robot's sonars.
     * @param sonarIdx the sonar's index.
     * @return the point from the given sonar.
     */
    public static Point3d getSensedPoint(RangeSensorBelt sonars, int sonarIdx) {
        double v;
        if (sonars.hasHit(sonarIdx))
            v = robotRadius + sonars.getMeasurement(sonarIdx);
        else
            v = robotRadius + sonars.getMaxRange();
        double x = v * Math.cos(sonars.getSensorAngle(sonarIdx));
        double z = v * Math.sin(sonars.getSensorAngle(sonarIdx));
        return new Point3d(x, 0, z);
    }

    /**
     * Calculates luminance from a given lux value.
     *
     * @param lux the lux from which the luminance will be calculated.
     * @return the luminance value.
     */
    public static double luxToLuminance(double lux) {
        return Math.pow(lux, 0.1);
    }

    /**
     * Calculates average luminance from two given lux values.
     *
     * @param lux1 the first lux value.
     * @param lux2 the second lux value.
     * @return the average luminance.
     */
    public static double luxToLuminance(double lux1, double lux2) {
        double lum1 = luxToLuminance(lux1);
        double lum2 = luxToLuminance(lux2);

        return (lum1 + lum2) / 2;
    }

    /**
     * Finds the index of the sonar with the minimum distance from an obstacle.
     *
     * @param sonars the robot's sonars.
     * @return the index of the sonar with the minimum distance from an obstacle
     */
    public static int getMinSonarIndex(RangeSensorBelt sonars) {
        int min = 0;
        for (int i = 1; i < sonars.getNumSensors(); i++) {
            if (sonars.getMeasurement(i) < sonars.getMeasurement(min))
                min = i;
        }

        return min;
    }

    /**
     * Finds the measurement of the sonar with the minimum distance from an obstacle.
     *
     * @param sonars the robot's sonars.
     * @return the measurement of the sonar with the minimum distance from an obstacle
     */
    public static double getMinSonarDist(RangeSensorBelt sonars) {
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < sonars.getNumSensors(); i++)
            if (sonars.getMeasurement(i) < min)
                min = sonars.getMeasurement(i);

        return min;
    }

    /**
     * Inner class which automatically calculates the right and left halfs of a line sensor when created
     * and has getters for them.
     */
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

        /**
         * Calculates the right and left halfs of a line sensor.
         *
         * @param line the line sensor.
         */
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
