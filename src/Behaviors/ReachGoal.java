package Behaviors;

import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.Agent;

import javax.vecmath.Color3f;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ReachGoal extends Behavior {
    private static final double LUMINANCE_STOP_POINT = 0.91;
    private final Agent robot;
    private final List<Color> colors;
    private int colorCounter;

    public ReachGoal(Sensors sensors, Agent robot) {
        super(sensors);
        this.robot = robot;
        colors = Arrays.asList(
                new Color(241, 255, 25),
                new Color(255, 143, 17),
                new Color(255, 26, 14),
                new Color(255, 31, 172),
                new Color(161, 53, 255),
                new Color(83, 71, 255),
                new Color(37, 195, 255),
                new Color(42, 255, 190),
                new Color(19, 255, 41),
                new Color(115, 255, 15),
                new Color(246, 255, 252));
        colorCounter = 0;
    }

    public Velocities act() {
        // Change color and increase the counter to get the next color at the next step.
        robot.setColor(new Color3f(colors.get(colorCounter % colors.size())));
        colorCounter++;

        // Stop moving.
        return new Velocities(0, 0);
    }

    public boolean isActive() {
        // If current luminance is equal or larger than the stop point, activate reach goal behavior.
        return SensorsInterpreter.luxToLuminance(getSensors().getLightR().getLux(), getSensors().getLightL().getLux())
                >= LUMINANCE_STOP_POINT;
    }
}