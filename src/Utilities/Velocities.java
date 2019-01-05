package Utilities;

public class Velocities {
    private double translationalVelocity;
    private double rotationalVelocity;

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

    @Override
    public String toString() {
        return "[Velocities: translationalVelocity=" + translationalVelocity
                + ", rotationalVelocity=" + rotationalVelocity + "]";
    }
}