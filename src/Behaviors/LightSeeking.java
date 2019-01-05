package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    public LightSeeking(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }
}