package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class GoStraight extends Behavior {
    public GoStraight(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        return new Velocities(TRANSLATIONAL_VELOCITY, 0.0);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}