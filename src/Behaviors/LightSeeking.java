package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

import static Utilities.Helpers.approximatelyEqual;

public class LightSeeking extends Behavior {
    private double minLuminance;

    public LightSeeking(Sensors sensors) {
        super(sensors);
        minLuminance = 0;
    }

    @Override
    public Velocities act() {
        // Get right and left luminance and use them to get currentLuminance too.
        double rLum = SensorsInterpreter.luxToLuminance(getSensors().getLightR().getLux());
        double lLum = SensorsInterpreter.luxToLuminance(getSensors().getLightL().getLux());
        double currentLuminance = (rLum + lLum) / 2;

        if (minLuminance == 0)
            // Set the previous luminance.
            minLuminance = currentLuminance;

        // Set rotational velocity, depending on the left and right luminance and init translational velocity.
        double rotationalVelocity = (lLum - rLum) * Math.PI * 4;
        double translationalVelocity = 0;

        // Τurn towards light and move only if the right luminance is almost equal with the left.
        if (approximatelyEqual(lLum, rLum, .2F)) {
            translationalVelocity = TRANSLATIONAL_VELOCITY;

            if (minLuminance > currentLuminance) {
                // Set previous luminance to current.
                minLuminance = currentLuminance;

                // If the luminance is decreasing, tilt towards light a bit,
                // because the robot may be moving in the opposite direction.
                rotationalVelocity = Math.PI / 7;
                if (rLum > lLum)
                    rotationalVelocity = -rotationalVelocity;
            }
        } else
            // Rotate a bit faster.
            rotationalVelocity *= 4;

        return new Velocities(translationalVelocity, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        // Always enable, since this is the lowest behavior of the subsumption hierarchy.
        return true;
    }
}