package Utilities;

public class Helpers {
    /**
     * Determine whether two numbers are approximately equal
     * by seeing if they are within a certain tolerance percentage.
     *
     * @param desiredValue        the value to be checked for approximation.
     * @param actualValue         the actual value.
     * @param tolerancePercentage the tolerance percentage.
     *                            <p>
     *                            ex. If 10 passed, 10% will be calculated.
     *                            <p>
     *                            If 0.1 -> 0.1% etc.
     * @return true if the desiredValue is approximately equal with the actualValue, within the tolerancePercentage,
     * otherwise false.
     */
    public static boolean approximatelyEqual(double desiredValue, double actualValue, float tolerancePercentage) {
        // Get the absolute difference of the values.
        double diff = Math.abs(desiredValue - actualValue);
        // Calculate tolerance.
        double tolerance = tolerancePercentage / 100 * desiredValue;
        // Return whether the absolute difference is smaller than the tolerance value or not.
        return diff < tolerance;
    }
}
