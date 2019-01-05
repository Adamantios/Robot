package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    private double prevLuminance;

    public LightSeeking(Sensors sensors) {
        super(sensors);
        prevLuminance = 0;
    }

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
            return new Velocities(TRANSLATIONAL_VELOCITY, Math.PI);
        }

        // Τurn towards light.
        double rotationalVelocity = (lLum - rLum) * Math.PI * 4;

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