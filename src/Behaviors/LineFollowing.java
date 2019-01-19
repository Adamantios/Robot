package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.LineSensor;

public class LineFollowing extends Behavior {

    public LineFollowing(Sensors sensors) {
        super(sensors);
    }

    @Override
    public Velocities act() {
        // Get line and lux halfs.
        SensorsInterpreter.LineSensorHalfs lineSensorHalfs =
                new SensorsInterpreter.LineSensorHalfs(getSensors().getLine());
        int rLine = lineSensorHalfs.getRight();
        int lLine = lineSensorHalfs.getLeft();
        double lLux = getSensors().getLightL().getLux();
        double rLux = getSensors().getLightR().getLux();

        // Tilt a bit towards the light to reassure that the line is not perpendicular.
        if (rLine == lLine && rLux <= lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, Math.PI);
        else if (rLine == lLine && rLux > lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, -Math.PI);

        // If there is a line but the lux is not coming from its side, do not follow it.
        if (lLine == 0 && rLine >= 2 && lLux > rLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, 0);
        if (rLine == 0 && lLine >= 2 && rLux > lLux)
            return new Velocities(TRANSLATIONAL_VELOCITY, 0);

        return new Velocities(TRANSLATIONAL_VELOCITY,
                (lLine - rLine) / (getSensors().getLine().getNumSensors() / 2D) * 4.8);
    }

    @Override
    public boolean isActive() {
        // Get line sensor.
        LineSensor line = getSensors().getLine();

        // If any line sensor has been hit, activate behavior.
        for (int i = 0; i < line.getNumSensors(); i++) {
            if (line.hasHit(i))
                return true;
        }

        // If no line sensor has been hit, do not activate behavior.
        return false;
    }
}