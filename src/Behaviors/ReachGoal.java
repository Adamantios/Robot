package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class ReachGoal extends Behavior {
    public ReachGoal(Sensors sensors) {
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