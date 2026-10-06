package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class BackBottom {

    private final CRServo backBottom;

    private static final double POWER = 0.5;

    private static final double FRONT_INTAKE_POWER = 0.5;

    private static final float DEADZONE = 0.08f;
//    private boolean lastAPressed = false;

    public BackBottom(CRServo backRoller) {
        this.backBottom = backRoller;
    }

    public void init() {
        if (backBottom != null) {
            backBottom.setDirection(DcMotorSimple.Direction.REVERSE);
            backBottom.setPower(0.0);
        }
    }

    public void update(int beltsMode,
                       float leftStickY,
                       boolean override,
                       boolean frontIntakeActive,
                       boolean aPressed) {
        if (backBottom == null) return;

        if (override) {
            backBottom.setPower(-1.0);
            return;
        }

        if (aPressed) {
            backBottom.setPower(-POWER);
            return;
        }

        if (Math.abs(leftStickY) >= DEADZONE) {
            backBottom.setPower(leftStickY > 0 ? POWER : -POWER);
            return;
        }

        backBottom.setPower(0.0);
    }

    public void stop() {
        if (backBottom != null) {
            backBottom.setPower(0.0);
        }
    }
}
