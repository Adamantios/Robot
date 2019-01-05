package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.LineSensor;

public class LineFollowing extends Behavior {
    private static final float MIN_LUMINANCE = 0.2F;

    public LineFollowing(Sensors sensors) {
        super(sensors);
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
        // TODO return false when luminance is being minimised.
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double currentLuminance = SensorsInterpreter.luxToLuminance(rLux, lLux);

        if (currentLuminance < MIN_LUMINANCE)
            return false;

        LineSensor line = getSensors().getLine();

        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i))
                return true;
        }

        return false;
    }
}