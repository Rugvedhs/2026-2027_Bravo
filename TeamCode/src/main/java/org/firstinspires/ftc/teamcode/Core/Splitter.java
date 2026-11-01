package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;

public class Splitter {

    private final CRServo Splitter;

    public Splitter(CRServo Splitter) {
        this.Splitter = Splitter;
    }

    public void init() {
        if (Splitter != null) {
            Splitter.setDirection(CRServo.Direction.FORWARD);
            Splitter.setPower(0.0);
        }
    }

    public void start() {
        if (Splitter != null) {
            Splitter.setPower(1.0);
        }
    }

    public void stop() {
        if (Splitter != null) {
            Splitter.setPower(0.0);
        }
    }

    public boolean isRunning() {
        return Splitter != null && Splitter.getPower() != 0.0;
    } // figure out if this is better than just ,setpower instead of the null statement same goes for the other 2
}
