package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
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

        double rotationalVelocity = (lLum - rLum) * ROTATIONAL_VELOCITY;

        return new Velocities(TRANSLATIONAL_VELOCITY, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        return true;
    }

    /**
     * Returns a description of this behavior.
     */
    @Override
    public String toString() {
        return "[LightSeeking: " + super.toString() + "]";
    }
}