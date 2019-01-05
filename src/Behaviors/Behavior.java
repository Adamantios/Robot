package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public abstract class Behavior {

    private Sensors sensors;

    private static final int ROTATION_COUNT = 20;

    static final double TRANSLATIONAL_VELOCITY = 0.4;

    static final double ROTATIONAL_VELOCITY = Math.PI / 2;

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