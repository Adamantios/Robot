package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.9;

    public ReachGoal(Sensors sensors) {
        super(sensors);
    }

    public Velocities act() {
        int numOfSensors = getSensors().getLine().getNumSensors();
        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int right = lineSensorHalfs.getRight();
        int left = lineSensorHalfs.getLeft();

        if (left == 0 && right == 0)
            return new Velocities(0, 0);
        else
            return new Velocities(TRANSLATIONAL_VELOCITY, (double) (left - right) / numOfSensors * 5);
    }

    public boolean isActive() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double currentLuminance = SensorsInterpreter.luxToLuminance(rLux, lLux);

        return currentLuminance >= LUMINANCE_STOP_POINT;
    }
}