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


    /**
     * Determine whether two numbers are approximately equal
     * by seeing if they are within a certain tolerance percentage.
     *
     * @param desiredValue        the value to be checked for approximation.
     * @param actualValue         the actual value.
     * @param tolerancePercentage the tolerance value. -> 1 = 1%, 2.5 = 2.5%, etc.
     * @return true if the desiredValue is approximately equal with the actualValue, within the tolerancePercentage.
     * Otherwise false.
     */
    private static boolean approximatelyEqual(double desiredValue, double actualValue, double tolerancePercentage) {
        double diff = Math.abs(desiredValue - actualValue);         //  1000 - 950  = 50
        double tolerance = tolerancePercentage / 100 * desiredValue;  //  20/100*1000 = 200
        return diff < tolerance;                                   //  50<200      = true
    }

    @Override
    public Velocities act() {
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();
        double rLum = SensorsInterpreter.luxToLuminance(rLux);
        double lLum = SensorsInterpreter.luxToLuminance(lLux);

        double currentLuminance = (rLum + lLum) / 2;

        // Set the previous luminance.
        if (prevLuminance == 0)
            prevLuminance = currentLuminance;
            // If the luminance is decreasing, tilt a bit, because the robot is probably moving in the opposite
            // direction.
        else if (prevLuminance > currentLuminance) {
            prevLuminance = currentLuminance;
            return new Velocities(TRANSLATIONAL_VELOCITY, Math.PI / 7);
        }

        // Τurn towards light and move only if the right luminance is almost equal with the left.
        double rotationalVelocity = (lLum - rLum) * Math.PI * 4;
        double translational;
        if (approximatelyEqual(lLum, rLum, .4) || approximatelyEqual(rLum, lLum, .4))
            translational = TRANSLATIONAL_VELOCITY;
        else {
            // Rotate a bit faster.
            rotationalVelocity *= 4;
            translational = 0;
        }

        return new Velocities(translational, rotationalVelocity);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}