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

        if (currentLuminance < MIN_LUMINANCE)
            return false;

        if (prevLuminance == 0)
            prevLuminance = currentLuminance;
        else if (prevLuminance > currentLuminance) {
            prevLuminance = currentLuminance;
            return false;
        }

        LineSensor line = getSensors().getLine();

        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i))
                return true;
        }

        return false;
    }
}