import simbad.gui.Simbad;

import javax.vecmath.Vector3d;

public class Main {
    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        // Create robot.
//        Robot robot = new Robot(new Vector3d(0.0D, 0.0D, 0.0D), "Manos Panos"); // center
//        Robot robot = new Robot(new Vector3d(-7.5D, 0.0D, -5.5D), "Manos Panos"); // top
        Robot robot = new Robot(new Vector3d(-8D, 0.0D, -6D), "Manos Panos"); // top from line
//        Robot robot = new Robot(new Vector3d(8.0D, 0.0D, 3.0D), "Manos Panos"); // down

        // Create environment.
        Environment environment = new Environment();

        // Add robot to the environment.
        environment.add(robot);

        // Run simbad.
        new Simbad(environment, false);
    }
}