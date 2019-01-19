package Utilities;

public class Velocities {
    private final double translationalVelocity;
    private final double rotationalVelocity;

    public Velocities(double translationalVelocity, double rotationalVelocity) {
        this.translationalVelocity = translationalVelocity;
        this.rotationalVelocity = rotationalVelocity;
    }

    public double getTranslationalVelocity() {
        return translationalVelocity;
    }

    public double getRotationalVelocity() {
        return rotationalVelocity;
    }
}