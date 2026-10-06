package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Belts {

    private final CRServo leftBelt;
    private final CRServo rightBelt;
    private static final double POWER = 1.0;
//    private boolean lastAPressed = false;

    // 0 = off, 1 = forward, 2 = reverse
    private int mode = 0;

    public Belts(CRServo leftBelt, CRServo rightBelt) {
        this.leftBelt = leftBelt;
        this.rightBelt = rightBelt;
    }

    public void init() {
        if (leftBelt != null) leftBelt.setDirection(DcMotorSimple.Direction.FORWARD);
        if (rightBelt != null) rightBelt.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void update(float rightStickY, boolean aPressed) {
        if (rightStickY > 0.0f) {
            mode = 1;
        } else if (rightStickY < 0.0f) {
            mode = 2;
        } else {
            mode = 0;
        }

        double leftPower;
        double rightPower;

        switch (mode) {
            case 1: // forward
                rightPower = POWER;
                leftPower = -POWER;
                break;
            case 2: // reverse
                rightPower = -POWER;
                leftPower = POWER;
                break;
            case 0:
            default:
                rightPower = 0.0;
                leftPower = 0.0;
                break;
        }

        if (aPressed) {
            rightPower = 1.0;
            leftPower = -1.0;
        }

        if (rightBelt != null) rightBelt.setPower(rightPower);
        if (leftBelt != null) leftBelt.setPower(leftPower);
    }

    public void stop() {
        mode = 0;
        if (rightBelt != null) rightBelt.setPower(0.0);
        if (leftBelt != null) leftBelt.setPower(0.0);
    }

    public int getMode() {
        return mode;
    }

    public boolean isRunning() {
        return mode != 0;
    }
}
