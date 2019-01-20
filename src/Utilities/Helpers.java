package Utilities;

public class Helpers {
    private final static float TOLERANCE_APPROXIMATION = .2F;

    /**
     * Determine whether two numbers are approximately equal
     * by seeing if they are within a certain tolerance percentage.
     *
     * @param desiredValue the value to be checked for approximation.
     * @param actualValue  the actual value.
     * @return true if the desiredValue is approximately equal with the actualValue, within the tolerancePercentage,
     * otherwise false.
     */
    public static boolean approximatelyEqual(double desiredValue, double actualValue) {
        // Get the absolute difference of the values.
        double diff = Math.abs(desiredValue - actualValue);
        // Calculate tolerance.
        double tolerance = TOLERANCE_APPROXIMATION / 100 * desiredValue;
        // Return whether the absolute difference is smaller than the tolerance value or not.
        return diff < tolerance;
    }
}
