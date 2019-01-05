package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.9;

    public ReachGoal(Sensors sensors) {
        super(sensors);
    }

    public Velocities act() {
        return new Velocities(0.0, 0.0);
    }

    public boolean isActive() {
        float lLum = getSensors().getLightL().getAverageLuminance();
        float rLum = getSensors().getLightR().getAverageLuminance();
        double currentLuminance = (lLum + rLum) / 2.0;

        return currentLuminance >= LUMINANCE_STOP_POINT;
    }
}