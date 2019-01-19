package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.91;

    public ReachGoal(Sensors sensors) {
        super(sensors);
    }

    public Velocities act() {
        // Stop moving.
        return new Velocities(0, 0);
    }

    public boolean isActive() {
        // If current luminance is equal or larger than the stop point, activate reach goal behavior.
        return SensorsInterpreter.luxToLuminance(getSensors().getLightR().getLux(), getSensors().getLightL().getLux())
                >= LUMINANCE_STOP_POINT;
    }
}