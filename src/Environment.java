import simbad.sim.*;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

class Environment extends EnvironmentDescription {
    Environment() {
        this.add(new Line(new Vector3d(-0, 0, -6), 4, this));
        this.add(new Line(new Vector3d(6, 0, 2), 4, this));
        Line l0 = new Line(new Vector3d(-9, 0, -6), 7f, this);
        l0.rotate90(1);
        this.add(l0);
        Line l1 = new Line(new Vector3d(-6, 0, -6), 2, this);
        l1.rotate90(1);
        this.add(l1);
        Line l2 = new Line(new Vector3d(-3, 0, -2), 5, this);
        l2.rotate90(1);
        this.add(l2);
        Line l3 = new Line(new Vector3d(2, 0, 2), 4, this);
        l3.rotate90(1);
        this.add(l3);
        this.add(new Arch(new Vector3d(6, 0, 4), this));
        this.add(new Wall(new Vector3d(-0.5, 0, 4), 10, 1.5f, this));
        this.add(new Wall(new Vector3d(12.5, 0, 4), 10, 1.5f, this));
        this.add(new Wall(new Vector3d(6, 0, 8), 6, 1.5f, this));

        Wall w1 = new Wall(new Vector3d(3, 0, 6), 4, 1.5f, this);
        w1.rotate90(1);
        this.add(w1);

        Wall w2 = new Wall(new Vector3d(9, 0, 6), 4, 1.5f, this);
        w2.rotate90(1);
        this.add(w2);

        this.add(new Box(new Vector3d(2, 0, 2), new Vector3f(3f, 1.5f, 4f), this));
        this.add(new Box(new Vector3d(-6, 0, -6), new Vector3f(2f, 1.5f, 2f), this));
        this.add(new Box(new Vector3d(-4, 0, 0.5), new Vector3f(2f, 1.5f, 7f), this));

        this.light1IsOn = true;
        this.light2IsOn = true;
        this.light1Color = new Color3f(1, 0, 0);
        this.light1Position = new Vector3d(6, 0, 6);
        this.light2Position = new Vector3d(6, 3, 6);

        // Add robot.
        this.add(new Robot(new Vector3d(0.0D, 0.0D, 0.0D), "Manos Panos robot"));
    }
}
