package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public abstract class Behavior {

    private Sensors sensors;

    static final double TRANSLATIONAL_VELOCITY = 0.35;

    Behavior(Sensors sensors) {
        this.sensors = sensors;
    }

    public abstract Velocities act();

    public abstract boolean isActive();

    Sensors getSensors() {
        return sensors;
    }

    @Override
    public String toString() {
        return "[Behavior: " + super.toString() + "]";
    }
}