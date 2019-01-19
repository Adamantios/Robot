package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Avoidance extends Behavior {
    private static final double K1 = 5;
    private static final double K2 = 0.5;
    private static final double K3 = 1;
    private static final double START_DISTANCE = .4;
    private static final double SAFETY_DISTANCE = .7;

    private static boolean clockwise;

    private boolean beganAvoidance;

    public Avoidance(Sensors sensors) {
        super(sensors);
        clockwise = true;
        beganAvoidance = false;
    }

    public static void setClockwise(boolean clockwise) {
        Avoidance.clockwise = clockwise;
    }

    /**
     * Finds the shortest angle from the given one.
     * <p>
     * If the given angle is larger than pi, then return a negative angle giving the same resulting position.
     * <p>
     * Acts similarly in case that the give angle is smaller than -pi.
     *
     * @param angle the angle.
     * @return the shortest angle.
     */
    private static double wrapToPi(double angle) {
        if (angle > Math.PI)
            return angle - Math.PI * 2;

        if (angle <= -Math.PI)
            return angle + Math.PI * 2;

        return angle;
    }

    public Velocities act() {
        // Get bumpers.
        RangeSensorBelt bumpers = getSensors().getBumpers();

        // If a bumper sensor on the front has been hit, go back and turn clockwise.
        // ------------------------- back ----------------- front -----------------.
        if (bumpers.getFrontQuadrantHits() > 0)
            return new Velocities(-TRANSLATIONAL_VELOCITY * 8, Math.PI);
        else if (bumpers.getBackQuadrantHits() > 0)
            return new Velocities(TRANSLATIONAL_VELOCITY * 8, Math.PI);

        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();

        // Get the point of the min sonar that hit and create its vector.
        Point3d p = SensorsInterpreter.getSensedPoint(sonars, SensorsInterpreter.getMinSonarIndex(sonars));
        double distance = p.distance(new Point3d(0, 0, 0));
        Vector3d vector = clockwise ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);

        // Create phLin and phRot using the distance and the vector found.
        double phLin = Math.atan2(vector.z, vector.x);
        double phRot = Math.atan(K3 * (distance - SAFETY_DISTANCE));

        // If the rotation is clockwise
        // or the left sonar has not been hit and the right sonar has been hit,
        // set the opposite phRot.
        if (clockwise || (!sonars.hasHit(3) && sonars.hasHit(9)))
            phRot = -phRot;

        // Get shortest phRef, using phLin and phRot.
        double phRef = wrapToPi(phLin + phRot);

        return new Velocities(K2 * Math.cos(phRef), K1 * phRef);
    }

    public boolean isActive() {
        // If any bumper has been hit, return true immediately.
        if (getSensors().getBumpers().oneHasHit())
            return true;

        // Get the right and left halfs of the line sensor.
        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int right = lineSensorHalfs.getRight();
        int left = lineSensorHalfs.getLeft();

        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();

        // Do not avoid obstacles if there is free space from the line's direction.
        for (int i = 0; i < getSensors().getLine().getNumSensors(); i++) {
            if (left > 0 && sonars.getLeftQuadrantMeasurement() == 0 ||
                    right > 0 && sonars.getRightQuadrantMeasurement() == 0)
                return false;
        }

        if (SensorsInterpreter.getMinSonarDist(sonars) <= START_DISTANCE && !beganAvoidance) {
            // Activate behavior and set the avoidance flag to true.
            beganAvoidance = true;
            return true;
        } else if (beganAvoidance) {
            // If any line sensor has been hit, do not activate avoidance behavior, otherwise enable.
            for (int i = 0; i < getSensors().getLine().getNumSensors(); i++) {
                if (getSensors().getLine().hasHit(i)) {
                    beganAvoidance = false;
                    return false;
                }
            }
            return true;
        }

        // If none of the above happens, do not activate avoidance behavior.
        return false;
    }
}