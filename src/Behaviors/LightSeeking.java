package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    private static final double LUMINANCE_SEEKING_MIN = 0.1;

    public LightSeeking(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        // Τurn towards light
        double rLux = getSensors().getLightR().getLux();
        double lLux = getSensors().getLightL().getLux();

        double rLum = SensorsInterpreter.luxToLuminance(rLux);
        double lLum = SensorsInterpreter.luxToLuminance(lLux);

        double translationalVelocity = TRANSLATIONAL_VELOCITY / (lLum + rLum);
        double rotationalVelocity = (lLum - rLum) * ROTATIONAL_VELOCITY;

        return new Velocities(translationalVelocity, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double currentLuminance = SensorsInterpreter.luxToLuminance(rLux, lLux);

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