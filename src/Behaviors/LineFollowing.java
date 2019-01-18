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

        return new Velocities(TRANSLATIONAL_VELOCITY, (double) (left - right) / numOfSensors * 5);
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
        if (rLux > lLux && right > left)
            return true;
        else return rLux < lLux && right < left;

//        // Get line sensor.
//        LineSensor line = getSensors().getLine();
//
//        // If any line sensor has been hit, activate behavior.
//        for (int i = 0; i < line.getNumSensors(); i++) {
//            if (line.hasHit(i))
//                return true;
//        }
//
//        // If none of the above happens, do not activate behavior.
//        return false;
    }
}