import javax.vecmath.Vector3d;

import Behaviors.*;
import Utilities.Sensors;
import Utilities.Velocities;
import simbad.sim.*;

import java.util.Arrays;


public class Robot extends Agent {
    private Behavior[] behaviors;
    private boolean[][] subsumes;
    private int currentBehaviorIndex;
    private Sensors sensors;

    Robot(Vector3d position, String name) {
        super(position, name);

        // Add sonars.
        RangeSensorBelt sonars = RobotFactory.addSonarBeltSensor(this, 12);

        // Add light sensors.
        LightSensor rLight = RobotFactory.addLightSensorRight(this);
        LightSensor lLight = RobotFactory.addLightSensorLeft(this);

        // Add bumpers.
        RangeSensorBelt bumpers = RobotFactory.addBumperBeltSensor(this, 8);

        // Add line sensors.
        LineSensor line = RobotFactory.addLineSensor(this, 11);

        // Create sensors object.
        sensors = new Sensors(sonars, bumpers, rLight, lLight, line);

        // Add camera for the line sensors.
        CameraSensor camera = RobotFactory.addCameraSensor(this);
        camera.rotateZ(-Math.PI / 4);

        initState();
    }

    private void initState() {
        behaviors = new Behavior[]{
                new ReachGoal(sensors),
                new Avoidance(sensors, this.getRadius()),
                new LineFollowing(sensors),
                new LightSeeking(sensors),
                new GoStraight(sensors)
        };
        subsumes = new boolean[][]{
                {false, true, true, true, true},
                {false, false, true, true, true},
                {false, false, false, true, true},
                {false, false, false, false, true},
                {false, false, false, false, false}};
    }

    @Override
    protected void initBehavior() {
        currentBehaviorIndex = 0;
    }

    public void performBehavior() {
        boolean[] isActive = new boolean[behaviors.length];

        for (int i = 0; i < isActive.length; i++) {
            isActive[i] = behaviors[i].isActive();
        }

        boolean ranABehavior = false;

        while (!ranABehavior) {
            boolean runCurrentBehavior = isActive[currentBehaviorIndex];

            if (runCurrentBehavior) {
                for (int i = 0; i < subsumes.length; i++) {
                    if (isActive[i] && subsumes[i][currentBehaviorIndex]) {
                        runCurrentBehavior = false;
                        break;
                    }
                }
            }

            if (runCurrentBehavior) {
                if (currentBehaviorIndex < behaviors.length) {
                    Velocities newVelocities = behaviors[currentBehaviorIndex].act();
                    this.setTranslationalVelocity(newVelocities.getTranslationalVelocity());
                    this.setRotationalVelocity(newVelocities.getRotationalVelocity());
                }

                ranABehavior = true;
            }

            if (behaviors.length > 0) {
                currentBehaviorIndex = (currentBehaviorIndex + 1) % behaviors.length;
            }
        }
    }

    @Override
    public String toString() {
        return "[BehaviorBasedAgent: behaviors=" + Arrays.toString(behaviors) + ", " +
                "subsumes=" + Arrays.deepToString(subsumes) + ", " +
                "currentBehaviorIndex=" + currentBehaviorIndex + ", " +
                "sensors=" + sensors + ", " + super.toString() + "]";
    }
}