package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.CRServo;

public class Pusher {
    private final CRServo RightPusher;
    private final CRServo LeftPusher;

    public Pusher(CRServo LeftPusher, CRServo RightPusher) {
        this.LeftPusher = LeftPusher;
        this.RightPusher = RightPusher;
    }

    public void init() {
        if (RightPusher != null) {
            RightPusher.setDirection(CRServo.Direction.FORWARD);
            RightPusher.setPower(0.0);
        }

        if (LeftPusher != null) {
            LeftPusher.setDirection(CRServo.Direction.REVERSE);
            LeftPusher.setPower(0.0);
        }
    }

    public void update(boolean toggleRight, boolean toggleLeft) {
        if (RightPusher != null) {
            RightPusher.setPower(toggleRight ? -1.0 : 0.0);
        }
        if (LeftPusher != null) {
            LeftPusher.setPower(toggleLeft ? 1.0 : 0.0); // figure out if this is better than just ,setpower instead of the null statement same goes for the other 2
        }
    }

    public void stop() {
        if (LeftPusher != null) LeftPusher.setPower(0.0);
        if (RightPusher != null) RightPusher.setPower(0.0);
    }
}
