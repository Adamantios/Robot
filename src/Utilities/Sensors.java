package Utilities;

import simbad.sim.LightSensor;
import simbad.sim.LineSensor;
import simbad.sim.RangeSensorBelt;

public class Sensors {
    private final RangeSensorBelt sonars, bumpers;
    private final LightSensor rLight, lLight;
    private final LineSensor line;

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
}