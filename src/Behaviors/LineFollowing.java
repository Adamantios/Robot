package Behaviors;

import Utilities.Sensors;
import Utilities.Velocities;
import simbad.sim.LineSensor;

public class LineFollowing extends Behavior {
    public LineFollowing(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        int left = 0, right = 0;
        float k = 0;
        LineSensor line = getSensors().getLine();

        for (int i = 0; i < line.getNumSensors() / 2; i++) {
            left += line.hasHit(i) ? 1 : 0;
            right += line.hasHit(line.getNumSensors() - i - 1) ? 1 : 0;
            k++;
        }

        return new Velocities(TRANSLATIONAL_VELOCITY, (left - right) / k * 5);
    }

    @Override
    public boolean isActive() {
        // TODO return false when luminance is being minimised.

        LineSensor line = getSensors().getLine();

        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i))
                return true;
        }

        return false;
    }
}