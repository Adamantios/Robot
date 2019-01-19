package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public abstract class Behavior {
    // The robot's translational velocity to be used.
    static final double TRANSLATIONAL_VELOCITY = 0.35;
    // The robot's sensors.
    private final Sensors sensors;

    /**
     * Constructor.
     *
     * @param sensors the sensors of the robot.
     */
    Behavior(Sensors sensors) {
        this.sensors = sensors;
    }

    /**
     * Calculates velocities depending on the current robot's behavior.
     *
     * @return the Velocities to be used.
     */
    public abstract Velocities act();

    /**
     * Returns whether the current behavior should be activated or not.
     *
     * @return true if the current behavior should be activated, otherwise false.
     */
    public abstract boolean isActive();

    /**
     * Getter for the robot's sensors.
     *
     * @return the robot's sensors.
     */
    Sensors getSensors() {
        return sensors;
    }
}