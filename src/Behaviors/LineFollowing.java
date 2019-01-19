package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.LineSensor;

public class LineFollowing extends Behavior {
    private static final float MIN_LUMINANCE = 0.2F;
    private double prevLuminance;

    public LineFollowing(Sensors sensors) {
        super(sensors);
        prevLuminance = 0;
    }

    @Override
    public Velocities act() {
        int numOfSensors = getSensors().getLine().getNumSensors();
        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int right = lineSensorHalfs.getRight();
        int left = lineSensorHalfs.getLeft();
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();

        // Tilt a bit to reassure that the line is not perpendicular.
        if (right == left && rLux <= lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, Math.PI);
        else if (right == left && rLux > lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, -Math.PI);

//        if (right > left && rLux > lLux)
//            return new Velocities(TRANSLATIONAL_VELOCITY, -Math.PI / 2);
//        else if (right < left && rLux < lLux) return new Velocities(TRANSLATIONAL_VELOCITY, Math.PI / 2);
//        else return new Velocities(TRANSLATIONAL_VELOCITY, 0);

        if (left == 0 && right >= 2 && lLux > rLux) {
            return new Velocities(TRANSLATIONAL_VELOCITY, 0);
        }

        if (right == 0 && left >= 2 && rLux > lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, 0);

        return new Velocities(TRANSLATIONAL_VELOCITY, (double) (left - right) / (numOfSensors / 2D) * 4.8);
    }

    @Override
    public boolean isActive() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double currentLuminance = SensorsInterpreter.luxToLuminance(rLux, lLux);

        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int right = lineSensorHalfs.getRight();
        int left = lineSensorHalfs.getLeft();

        // Follow the line only if the luminance is coming from the line's side.
//        if (rLux > lLux && right > left)
//            return true;
//        else return rLux < lLux && right < left;

        // Get line sensor.
        LineSensor line = getSensors().getLine();

        // If any line sensor has been hit, activate behavior.
        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i))
                return true;
        }

        // If none of the above happens, do not activate behavior.
        return false;
    }
}