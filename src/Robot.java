import javax.vecmath.Vector3d;

import simbad.sim.*;


public class Robot extends Agent {
    private RangeSensorBelt sonars;
    private LightSensor r_light;
    private LightSensor l_light;
    private RangeSensorBelt bumpers;
    private LineSensor line;

    Robot(Vector3d position, String name) {
        super(position, name);

        // Add sonars.
        sonars = RobotFactory.addSonarBeltSensor(this, 12);

        // Add light sensors.
        r_light = RobotFactory.addLightSensorRight(this);
        l_light = RobotFactory.addLightSensorLeft(this);

        // Add bumpers.
        bumpers = RobotFactory.addBumperBeltSensor(this, 8);

        // Add line sensors.
        line = RobotFactory.addLineSensor(this, 11);
        CameraSensor camera = RobotFactory.addCameraSensor(this);
        camera.rotateZ(-Math.PI / 4);
    }

    public void initBehavior() {

    }

    public void performBehavior() {

    }
}
