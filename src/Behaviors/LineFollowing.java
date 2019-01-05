package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class LineFollowing extends Behavior {
    public LineFollowing(Sensors sensors) {
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