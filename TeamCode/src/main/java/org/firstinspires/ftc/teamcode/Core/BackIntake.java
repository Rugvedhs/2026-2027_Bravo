package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class BackIntake {
    private final CRServo backIntake;
    private static final float DEADZONE = 0.08f;
    private static final double POWER = 1.0;
    private static final double BELTS_SLOW_POWER = 0.1; // slow speed when belts active

    public BackIntake(CRServo backIntake) {
        this.backIntake = backIntake;
    }

    public void init() {
        if (backIntake != null) {
            backIntake.setDirection(DcMotorSimple.Direction.FORWARD);
            backIntake.setPower(0.0);
        }
    }

    public void update(float leftStickY, boolean override, int beltsMode, boolean aPressed) {
        if (backIntake == null) return;

        if (aPressed) {
            backIntake.setPower(-POWER);
            return;
        }

        if (override) {
            backIntake.setPower(-POWER);
            return;
        }

        if (Math.abs(leftStickY) < DEADZONE) {
            backIntake.setPower(0.0);
            return;
        }
        backIntake.setPower(leftStickY > 0 ? -POWER : POWER);
    }

    public void stop() {
        if (backIntake != null) {
            backIntake.setPower(0.0);
        }
    }
}
