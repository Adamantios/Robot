package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    private static final double LUMINANCE_SEEKING_MIN = 0.1;

    public LightSeeking(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        double lLum = getSensors().getLeftLuminance();
        double rLum = getSensors().getRightLuminance();

        // Move towards light.
        double translationalVelocity = TRANSLATIONAL_VELOCITY / (lLum + rLum);
        // Τurn towards light.
        double rotationalVelocity = (lLum - rLum) * ROTATIONAL_VELOCITY;

        return new Velocities(translationalVelocity, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        // Get current average luminance.
        double currentLuminance = getSensors().getAverageLuminance();
        // Seek light only if it's near.
        return currentLuminance > LUMINANCE_SEEKING_MIN;
    }

    /**
     * Returns a description of this behavior.
     */
    @Override
    public String toString() {
        return "[LightSeeking: " + super.toString() + "]";
    }
}