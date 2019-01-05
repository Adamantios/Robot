import simbad.gui.Simbad;

import javax.vecmath.Vector3d;

public class Main {
    /**
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        // Create robot.
        Robot robot = new Robot(new Vector3d(0.0D, 0.0D, 0.0D), "Manos Panos robot");

        // Create environment.
        Environment environment = new Environment();

        // Add robot to the environment.
        environment.add(robot);

        // Run simbad.
        new Simbad(environment, false);
    }
}