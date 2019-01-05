package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.RangeSensorBelt;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class Avoidance extends Behavior {
    private static final boolean CLOCKWISE = true;
    private static final double K1 = 5;
    private static final double K2 = 0.8;
    private static final double K3 = 1;
    private static final double START_DISTANCE = 0.4;
    private static final double SAFETY_DISTANCE = 0.6;

    private float robotRadius;

    public Avoidance(Sensors sensors, float robotRadius) {
        super(sensors);
        this.robotRadius = robotRadius;
    }

    private static double wrapToPi(double a) {
        if (a > Math.PI)
            return a - Math.PI * 2;

        if (a <= -Math.PI)
            return a + Math.PI * 2;

        return a;
    }

    public Velocities act() {
        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();
        // Get min sonar's index.
        int min = SensorsInterpreter.getMinSonarIndex(sonars);

        Point3d p = SensorsInterpreter.getSensedPoint(robotRadius, sonars, min);
        double d = p.distance(new Point3d(0, 0, 0));
        Vector3d v;
        v = CLOCKWISE ? new Vector3d(-p.z, 0, p.x) : new Vector3d(p.z, 0, -p.x);
        double phLin = Math.atan2(v.z, v.x);
        double phRot = Math.atan(K3 * (d - SAFETY_DISTANCE));

        if (CLOCKWISE)
            phRot = -phRot;

        double phRef = wrapToPi(phLin + phRot);

        return new Velocities(K2 * Math.cos(phRef), K1 * phRef);
    }

    public boolean isActive() {
        // Get sonars.
        RangeSensorBelt sonars = getSensors().getSonars();
        // Get minimum distance between robot and obstacles, according to sonars measurements.
        double min = SensorsInterpreter.getMinSonar(sonars);

        return getSensors().getBumpers().oneHasHit() || min <= START_DISTANCE;

    }
}