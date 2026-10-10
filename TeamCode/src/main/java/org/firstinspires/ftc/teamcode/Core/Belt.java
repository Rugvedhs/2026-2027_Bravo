package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Belt {

    private final CRServo beltServo;
    private static final double POWER = 1.0;

    // 0 = off, 1 = forward, 2 = reverse
    private int mode = 0;

    public Belt(CRServo beltServo) {
        this.beltServo = beltServo;
    }

    public void init() {
        if (beltServo != null) {
            beltServo.setDirection(DcMotorSimple.Direction.FORWARD);
            beltServo.setPower(0.0);
        }
    }

    public void update(float stickY, boolean aPressed) {
        if (beltServo == null) return;

        if (aPressed) {
            beltServo.setPower(POWER);
            mode = 1;
            return;
        }

        if (stickY > 0.0f) {
            beltServo.setPower(POWER);
            mode = 1;
        } else if (stickY < 0.0f) {
            beltServo.setPower(-POWER);
            mode = 2;
        } else {
            beltServo.setPower(0.0);
            mode = 0;
        }
    }

    public void stop() {
        mode = 0;
        if (beltServo != null) beltServo.setPower(0.0);
    }

    public void setPower(double power) {
        if (beltServo != null) beltServo.setPower(power);
    }

    public int getMode() {
        return mode;
    }

    public boolean isRunning() {
        return mode != 0;
    }
}
