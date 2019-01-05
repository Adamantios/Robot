package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public abstract class Behavior {

    private Sensors sensors;

    private static final int ROTATION_COUNT = 20;

    protected static final double TRANSLATIONAL_VELOCITY = 0.4;

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

    public static int getRotationCount() {
        return ROTATION_COUNT;
    }
}