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

        // Do not follow the line if luminance is less than allowed.
        if (currentLuminance < MIN_LUMINANCE)
            return false;

        // Set luminance if it hasn't been set.
        if (prevLuminance == 0)
            prevLuminance = currentLuminance;
        else if (prevLuminance > currentLuminance) {
            prevLuminance = currentLuminance;
            // Do not follow the line if luminance is being minimised.
            return false;
        }

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