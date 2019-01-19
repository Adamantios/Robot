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
    private static final double START_DISTANCE = .4;//0.4
    private static final double SAFETY_DISTANCE = .7; //0.5

    private static boolean clockwise = true;

    private double prevLuminance;
    private float robotRadius;
    private boolean completed;
    private double initialLum;
    private boolean circleCompleted;
    private double hitPoint;
    private double leavePoint;
    private double bestPoint;
    private boolean beganAvoidance;

    public Avoidance(Sensors sensors, float robotRadius) {
        super(sensors);
        prevLuminance = 0;
        beganAvoidance = false;
        this.robotRadius = robotRadius;
    }

    public static void setClockwise(boolean clockwise) {
        Avoidance.clockwise = clockwise;
    }

    private static double wrapToPi(double a) {
        if (a > Math.PI)
            return a - Math.PI * 2;

        if (a <= -Math.PI)
            return a + Math.PI * 2;

        return a;
    }

    public Velocities act() {
        if (getSensors().getBumpers().getFrontQuadrantHits() > 0)
            return new Velocities(-TRANSLATIONAL_VELOCITY * 8, Math.PI);
        else if (getSensors().getBumpers().getBackQuadrantHits() > 0)
            return new Velocities(TRANSLATIONAL_VELOCITY * 8, Math.PI);

        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();

        // Get min sonar's index.
        int min = SensorsInterpreter.getMinSonarIndex(sonars);
        Point3d p = SensorsInterpreter.getSensedPoint(robotRadius, sonars, min);
        double distance = p.distance(new Point3d(0, 0, 0));
        Vector3d vector = clockwise ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);
        double phLin = Math.atan2(vector.z, vector.x);
        double phRot = Math.atan(K3 * (distance - SAFETY_DISTANCE));

        if (clockwise || (!sonars.hasHit(3) && sonars.hasHit(9)))
            phRot = -phRot;

        double phRef = wrapToPi(phLin + phRot);

        return new Velocities(K2 * Math.cos(phRef), K1 * phRef);
    }

    public boolean isActive() {
        if (getSensors().getBumpers().oneHasHit())
            return true;

        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int right = lineSensorHalfs.getRight();
        int left = lineSensorHalfs.getLeft();

        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();

//        if (sonars.hasHit(3) && sonars.hasHit(9))
//            return false;

        // Do not avoid obstacles if there is free space from the line's direction.
        for (int i = 0; i < getSensors().getLine().getNumSensors(); i++) {
            if (left > 0 && sonars.getLeftQuadrantMeasurement() == 0 ||
                    right > 0 && sonars.getRightQuadrantMeasurement() == 0)
                return false;
        }

        // Get minimum distance between robot and obstacles, according to sonars measurements.
        double min = SensorsInterpreter.getMinSonar(sonars);

        if (min <= START_DISTANCE && !beganAvoidance) {
            beganAvoidance = true;
            return true;
        } else if (beganAvoidance) {
            // If any line sensor has been hit, deactivate avoidance.
            for (int i = 0; i < getSensors().getLine().getNumSensors(); i++) {
                if (getSensors().getLine().hasHit(i)) {
                    beganAvoidance = false;
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}