package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.93;

    public ReachGoal(Sensors sensors) {
        super(sensors);
    }

    public Velocities act() {
        return new Velocities(0, 0);
    }

    public boolean isActive() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double currentLuminance = SensorsInterpreter.luxToLuminance(rLux, lLux);

        return currentLuminance >= LUMINANCE_STOP_POINT;
    }
}