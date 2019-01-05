package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class Avoidance extends Behavior {
    public Avoidance(Sensors sensors) {
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
