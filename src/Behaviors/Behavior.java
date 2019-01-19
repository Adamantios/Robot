package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public abstract class Behavior {

    private Sensors sensors;

    static final double TRANSLATIONAL_VELOCITY = 0.35;

    static boolean CLOCKWISE = true;

    Behavior(Sensors sensors) {
        this.sensors = sensors;
    }

    public static void setCLOCKWISE(boolean CLOCKWISE) {
        Behavior.CLOCKWISE = CLOCKWISE;
    }

    public abstract Velocities act();

    public abstract boolean isActive();

    Sensors getSensors() {
        return sensors;
    }
}