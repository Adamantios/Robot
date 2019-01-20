package Utilities;

public class Helpers {
    /**
     * Determine whether two numbers are approximately equal
     * by seeing if they are within a certain tolerance percentage.
     *
     * @param value1              the first value.
     * @param value2              the second value.
     * @param tolerancePercentage the tolerance percentage.
     *                            <p>
     *                            ex. If 10 passed, 10% will be calculated.
     *                            <p>
     *                            If 0.1 -> 0.1% etc.
     * @return true if the values are approximately equal, within the tolerancePercentage, otherwise false.
     */
    public static boolean approximatelyEqual(double value1, double value2, float tolerancePercentage) {
        // Get the absolute difference of the values.
        double diff = Math.abs(value1 - value2);

        // Calculate tolerances.
        double tolerance1 = tolerancePercentage / 100 * value1;
        double tolerance2 = tolerancePercentage / 100 * value2;

        // Return whether the absolute difference is smaller than the tolerance values or not.
        return diff < tolerance1 || diff < tolerance2;
    }
}
