import Behaviors.*;
import Utilities.Sensors;
import Utilities.SensorsInterpreter;
import Utilities.Velocities;
import simbad.sim.*;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import java.awt.*;

import static Utilities.Helpers.approximatelyEqual;


class Robot extends Agent {
    private final Sensors sensors;
    private Behavior[] behaviors;
    private boolean[][] subsumes;
    private int currentBehaviorIndex;
    private boolean checkCompleted;
    private int checkTurnCount;
    private double maxLuminanceDetected;

    Robot(Vector3d position) {
        // Ma.Pa. is an acronym for Manos and Panos, the developers.
        super(position, "Ma.Pa.");
        checkCompleted = false;
        checkTurnCount = 80;
        maxLuminanceDetected = 0;

        // Set robot's color.
        setColor(new Color3f(new Color(255, 53, 162)));

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

        // Set initial values for starting check.
        checkCompleted = false;
        checkTurnCount = 80;
        maxLuminanceDetected = 0;
    }

    @Override
    protected void initBehavior() {
        // Initialise behaviors and subsumption matrix.
        initState();
    }

    /**
     * Compares the current luminance with the maximum detected, storing the maximum every time
     * and sets rotational velocity to pi/2.
     */
    private void searchForLuminance() {
        // Set Velocities.
        this.setTranslationalVelocity(0);
        this.setRotationalVelocity(Math.PI / 2);

        // Get current luminance.
        double currentLuminance = SensorsInterpreter.luxToLuminance(sensors.getLightR().getLux(),
                sensors.getLightL().getLux());

        // Store the maximum luminance.
        if (maxLuminanceDetected < currentLuminance)
            maxLuminanceDetected = currentLuminance;

        // Increase the check's turn counter.
        checkTurnCount--;
    }

    /**
     * Sets rotational velocity to pi/2 if the maximum luminance is not approximately equal with the current one.
     * <p>
     * Otherwise, flags the initial check as completed.
     */
    private void turnTowardsLight() {
        // Get current luminance.
        double currentLuminance = SensorsInterpreter.luxToLuminance(sensors.getLightR().getLux(),
                sensors.getLightL().getLux());

        if (approximatelyEqual(currentLuminance, maxLuminanceDetected, .005F))
            checkCompleted = true;
        else
            this.setRotationalVelocity(Math.PI / 2);
    }

    /**
     * Rotates the robot, locates where the maximum luminance is coming from and points there.
     */
    private void performInitialCheck() {
        if (checkTurnCount > 0)
            // Check for maximum luminance in the room.
            searchForLuminance();

        else if (!checkCompleted && maxLuminanceDetected > 0) {
            // Turn towards the location where the maximum luminance was detected.
            turnTowardsLight();

            // Set anticlockwise rotation if the luminance on the right is less than the left.
            if (sensors.getLightR().getAverageLuminance() < sensors.getLightL().getAverageLuminance())
                Avoidance.setClockwise(false);
            else
                Avoidance.setClockwise(true);
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
        performInitialCheck();

        if (checkCompleted)
            chooseBehavior();
    }
}