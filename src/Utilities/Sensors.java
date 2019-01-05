package Utilities;

import simbad.sim.LightSensor;
import simbad.sim.LineSensor;
import simbad.sim.RangeSensorBelt;

public class Sensors {

    private RangeSensorBelt sonars, bumpers;
    private LightSensor rLight;
    private LightSensor lLight;
    private LineSensor line;

    public Sensors(RangeSensorBelt sonars, RangeSensorBelt bumpers, LightSensor rLight, LightSensor lLight,
                   LineSensor line) {
        this.sonars = sonars;
        this.bumpers = bumpers;
        this.rLight = rLight;
        this.lLight = lLight;
        this.line = line;
    }

    public RangeSensorBelt getSonars() {
        return sonars;
    }

    public RangeSensorBelt getBumpers() {
        return bumpers;
    }

    public LightSensor getLightR() {
        return rLight;
    }

    public LightSensor getLightL() {
        return lLight;
    }

    public LineSensor getLine() {
        return line;
    }

    public double getRightLuminance() {
        double rLum = rLight.getLux();
        return Math.pow(rLum, 0.1);
    }

    public double getLeftLuminance() {
        double lLum = lLight.getLux();
        return Math.pow(lLum, 0.1);
    }

    public double getAverageLuminance() {
        double lLum = rLight.getLux();
        double rLum = lLight.getLux();

        lLum = Math.pow(lLum, 0.1);
        rLum = Math.pow(rLum, 0.1);

        return (lLum + rLum) / 2.0;
    }

    @Override
    public String toString() {
        return "[Sensors: sonars=" + sonars + ", " +
                "bumpers=" + bumpers + ", " +
                "lightSensorLeft=" + lLight + ", " +
                "lightSensorRight=" + rLight + "]";
    }
}