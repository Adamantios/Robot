package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class GoStraight extends Behavior {
    public GoStraight(Sensors sensors) {
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