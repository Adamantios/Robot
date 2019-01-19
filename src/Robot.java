import javax.vecmath.Vector3d;

import Behaviors.*;
import Utilities.Sensors;
import Utilities.Velocities;
import simbad.sim.*;


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
                new LightSeeking(sensors)
        };
        subsumes = new boolean[][]{
                {false, true, true, true},
                {false, false, true, true},
                {false, false, false, true},
                {false, false, false, false}};
    }

    @Override
    protected void initBehavior() {
        currentBehaviorIndex = 0;

        // Set anticlockwise rotation if the lux and the distance on the right are bigger.
        double lLux = sensors.getLightL().getLux();
        double rLux = sensors.getLightR().getLux();
        double front_right = sensors.getSonars().getFrontRightQuadrantMeasurement();
        double front_left = sensors.getSonars().getFrontLeftQuadrantMeasurement();
        if (front_right > front_left && rLux > lLux)
            Behavior.setCLOCKWISE(false);
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
//                    TODO remove
//                    System.out.println("Running behavior " + behaviors[currentBehaviorIndex].toString());


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
}