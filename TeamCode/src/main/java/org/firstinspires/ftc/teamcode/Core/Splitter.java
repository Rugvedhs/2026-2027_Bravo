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

    /** Starts Splitter forward at 100% power */
    public void start() {
        if (Splitter != null) {
            Splitter.setPower(1.0);
        }
    }

    /** Reverses Splitter at -100% power to clear jams */
    public void reverse() {
        if (Splitter != null) {
            Splitter.setPower(-1.0);
        }
    }

    /** Stops the Splitter servo */
    public void stop() {
        if (Splitter != null) {
            Splitter.setPower(0.0);
        }
    }

    /** Dynamically checks if the servo is currently receiving power */
    public boolean isRunning() {
        return Splitter != null && Splitter.getPower() != 0.0;
    }
}
