package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;

public class LightSeeking extends Behavior {
    private final static float TOLERANCE_APPROXIMATION = .4F;
    private double minLuminance;

    public LightSeeking(Sensors sensors) {
        super(sensors);
        minLuminance = 0;
    }

    /**
     * Determine whether two numbers are approximately equal
     * by seeing if they are within a certain tolerance percentage.
     *
     * @param desiredValue the value to be checked for approximation.
     * @param actualValue  the actual value.
     * @return true if the desiredValue is approximately equal with the actualValue, within the tolerancePercentage,
     * otherwise false.
     */
    private static boolean approximatelyEqual(double desiredValue, double actualValue) {
        // Get the absolute difference of the values.
        double diff = Math.abs(desiredValue - actualValue);
        // Calculate tolerance.
        double tolerance = TOLERANCE_APPROXIMATION / 100 * desiredValue;
        // Return whether the absolute difference is smaller than the tolerance value or not.
        return diff < tolerance;
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
        if (approximatelyEqual(lLum, rLum) || approximatelyEqual(rLum, lLum)) {
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