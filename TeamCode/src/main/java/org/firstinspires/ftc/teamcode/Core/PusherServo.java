package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class PusherServo {
    private final CRServo RightPusherServo;
    private final CRServo LeftPusherServo;

    public PusherServo(CRServo LeftPusherServo, CRServo RightPusherServo) {
        this.LeftPusherServo = LeftPusherServo;
        this.RightPusherServo = RightPusherServo;
    }

    public void init() {
        if (RightPusherServo != null) {
            RightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);
            RightPusherServo.setPower(0.0);
        }

        if (LeftPusherServo != null) {
            LeftPusherServo.setDirection(DcMotorSimple.Direction.REVERSE); // Set REVERSE for opposite mounting
            LeftPusherServo.setPower(0.0);
        }
    }

    public void update(boolean toggleRight, boolean toggleLeft) {
        if (RightPusherServo != null) {
            RightPusherServo.setPower(toggleRight ? 1.0 : 0.0);
        }
        if (LeftPusherServo != null) {
            LeftPusherServo.setPower(toggleLeft ? 1.0 : 0.0);
        }
    }

    public void stop() {
        if (LeftPusherServo != null) LeftPusherServo.setPower(0.0);
        if (RightPusherServo != null) RightPusherServo.setPower(0.0);
    }
}
