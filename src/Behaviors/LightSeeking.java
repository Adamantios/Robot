package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    public LightSeeking(Sensors sensors) {
        super(sensors);
    }

    private double prevLuminance = 0;

    @Override
    public Velocities act() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double rLum = SensorsInterpreter.luxToLuminance(rLux);
        double lLum = SensorsInterpreter.luxToLuminance(lLux);

        double currentLuminance = (rLum + lLum) / 2;

        if (prevLuminance == 0)
            prevLuminance = currentLuminance;
        else if (prevLuminance > currentLuminance) {
            prevLuminance = currentLuminance;
            return new Velocities(TRANSLATIONAL_VELOCITY, ROTATIONAL_VELOCITY);
        }

        // Τurn towards light.
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