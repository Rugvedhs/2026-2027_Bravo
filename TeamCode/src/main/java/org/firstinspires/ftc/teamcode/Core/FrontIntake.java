package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FrontIntake {
    private final DcMotor frontIntake;
    public FrontIntake(DcMotor frontIntake) {
        this.frontIntake = frontIntake;
    }
    public void init() {
        if (frontIntake != null) {
            frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
            frontIntake.setPower(0.0);
        }
    }
    public void update(boolean yPressed, boolean aPressed) {
        if (frontIntake == null) return;
        if (yPressed) {
            frontIntake.setPower(1.0);
        } else if (aPressed) {
            frontIntake.setPower(-1.0);
        } else {
            frontIntake.setPower(0.0);
        }
    }
    public void stop() {
        if (frontIntake != null) {
            frontIntake.setPower(0.0);
        }
    }
}
