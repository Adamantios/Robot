import Behaviors.*;
import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.*;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import java.awt.*;


class Robot extends Agent {
    private static final int ODOMETER_CHECK_INTERVAL = 70;
    private final Sensors sensors;
    private Behavior[] behaviors;
    private boolean[][] subsumes;
    private int currentBehaviorIndex;
    private float timesRotationChanged;

    Robot(Vector3d position) {
        super(position, "Ma.Pa.");
        // Set robot's color.
        setColor(new Color3f(new Color(63, 69, 136)));
        timesRotationChanged = 0;

        // Add sonars.
        RangeSensorBelt sonars = RobotFactory.addSonarBeltSensor(this, 12);

        // Add light sensors.
        LightSensor rLight = RobotFactory.addLightSensorRight(this);
        LightSensor lLight = RobotFactory.addLightSensorLeft(this);

        // Add bumpers.
        RangeSensorBelt bumpers = RobotFactory.addBumperBeltSensor(this, 8);

        // Add line sensors.
        LineSensor line = RobotFactory.addLineSensor(this, 11);

        // Create sensors instance.
        sensors = new Sensors(sonars, bumpers, rLight, lLight, line);

        // Add camera for the line sensors.
        CameraSensor camera = RobotFactory.addCameraSensor(this);
        camera.rotateZ(-Math.PI / 4);
    }

    private void initState() {
        // Create behaviors.
        behaviors = new Behavior[]{
                new ReachGoal(sensors),
                new Avoidance(sensors),
                new LineFollowing(sensors),
                new LightSeeking(sensors)
        };

        // Create subsumption matrix.
        subsumes = new boolean[][]{
                {false, true, true, true},
                {false, false, true, true},
                {false, false, false, true},
                {false, false, false, false}};

        // Set current behavior to zero.
        currentBehaviorIndex = 0;

        // Set the robot's radius for the sensors interpreting calculations.
        SensorsInterpreter.setRobotRadius(this.getRadius());

        // Set rotation changed times to 0.
        timesRotationChanged = 0;
    }

    @Override
    protected void initBehavior() {
        // Initialise behaviors and subsumption matrix.
        initState();

        // Set anticlockwise rotation if the lux and the distance on the right are bigger.
        double lLux = sensors.getLightL().getLux();
        double rLux = sensors.getLightR().getLux();
        double front_right = sensors.getSonars().getFrontRightQuadrantMeasurement();
        double front_left = sensors.getSonars().getFrontLeftQuadrantMeasurement();
        if (front_right > front_left && rLux > lLux)
            Avoidance.setClockwise(false);
    }

    /**
     * Checks odometer and reverses rotation if a certain interval has been reached.
     */
    private void checkOdometer() {
        if (getOdometer() >= (timesRotationChanged + 1) * ODOMETER_CHECK_INTERVAL
                && getOdometer() % ODOMETER_CHECK_INTERVAL <= .5) {
            Avoidance.reverseRotation();
            timesRotationChanged++;
        }
    }

    /**
     * Chooses and runs one of the behaviors depending on their states.
     */
    private void chooseBehavior() {
        // Create array to keep the activation state of the behaviors.
        boolean[] isActive = new boolean[behaviors.length];

        // Get the state of all the behaviors.
        for (int i = 0; i < isActive.length; i++)
            isActive[i] = behaviors[i].isActive();

        // No behavior has been ran yet.
        boolean ranABehavior = false;

        // Loop until a behavior is ran.
        while (!ranABehavior) {
            // Get current behavior's state.
            boolean runCurrentBehavior = isActive[currentBehaviorIndex];

            if (runCurrentBehavior) {
                for (int i = 0; i < subsumes.length; i++)
                    if (isActive[i] && subsumes[i][currentBehaviorIndex]) {
                        // If the current behavior is being suppressed, set its state to false and break.
                        runCurrentBehavior = false;
                        break;
                    }
            }

            if (runCurrentBehavior) {
                if (currentBehaviorIndex < behaviors.length) {
                    // Run current behavior.
                    Velocities newVelocities = behaviors[currentBehaviorIndex].act();
                    this.setTranslationalVelocity(newVelocities.getTranslationalVelocity());
                    this.setRotationalVelocity(newVelocities.getRotationalVelocity());
                }

                // A behavior has just been ran.
                ranABehavior = true;
            }

            if (behaviors.length > 0)
                // Reset current behavior's index if necessary.
                currentBehaviorIndex = (currentBehaviorIndex + 1) % behaviors.length;
        }
    }

    public void performBehavior() {
//        checkOdometer();
        chooseBehavior();
    }
}