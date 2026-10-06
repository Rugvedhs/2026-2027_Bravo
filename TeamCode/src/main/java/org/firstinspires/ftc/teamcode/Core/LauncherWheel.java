package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LauncherWheel {

    private final DcMotor launcherWheel;
    private static final double POWER = 1; //.35
    private boolean toggledB = false;
    private boolean lastBPressed = false;

    public LauncherWheel(DcMotor launcherWheel) {
        this.launcherWheel = launcherWheel;
    }

    public void init() {
        if (launcherWheel != null) {
            launcherWheel.setDirection(DcMotorSimple.Direction.FORWARD);
            launcherWheel.setPower(0.0);
        }
    }

    // UPDATED METHOD
    public void update(boolean bPressed, boolean override, boolean aPressed) {
        if (launcherWheel == null) return;

        // 1) Maintain toggle state for B (edge detection)
        if (bPressed && !lastBPressed) {
            toggledB = !toggledB;
        }
        lastBPressed = bPressed;

        // 2) A hold should immediately run the launcher while held (highest priority)
        if (aPressed) {
            launcherWheel.setPower(-POWER);
            return;
        }

        // 3) Override (Y) next priority
        if (override) {
            launcherWheel.setPower(-1.0);
            return;
        }

        // 4) Otherwise follow toggledB state
        launcherWheel.setPower(toggledB ? -POWER : 0.0);
    }

    public void stop() {
        toggledB = false;
        if (launcherWheel != null) {
            launcherWheel.setPower(0.0);
        }
    }

    public void setPower(double level){
        if (launcherWheel != null) {
            launcherWheel.setPower(level);
        }
    }


}
