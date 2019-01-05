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
        // Τurn towards light
        double lLum = getSensors().getLightL().getLux();
        double rLum = getSensors().getLightR().getLux();

        lLum = (float) Math.pow(lLum, 0.1);
        rLum = (float) Math.pow(rLum, 0.1);

        double translationalVelocity = TRANSLATIONAL_VELOCITY / (lLum + rLum);
        double rotationalVelocity = (lLum - rLum) * ROTATIONAL_VELOCITY;

        return new Velocities(translationalVelocity, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        float lLum = getSensors().getLightL().getAverageLuminance();
        float rLum = getSensors().getLightR().getAverageLuminance();
        double luminance = (lLum + rLum) / 2.0;

        // Seek light only if it's near.
        return luminance > LUMINANCE_SEEKING_MIN;
    }

    /*
     * Returns a description of this behavior.
     */
    @Override
    public String toString() {
        return "[LightSeeking: " + super.toString() + "]";
    }
}