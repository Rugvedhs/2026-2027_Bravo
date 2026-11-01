package org.firstinspires.ftc.teamcode.Core;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class FrontIntake {
    private final DcMotorEx FrontIntake;

    public FrontIntake(DcMotorEx FrontIntake) {
        this.FrontIntake = FrontIntake;
    }

    public void init() {
        if (FrontIntake != null) {
            FrontIntake.setDirection(DcMotorEx.Direction.FORWARD);
            FrontIntake.setPower(0.0);
        }
    }

    public void update(boolean yPressed, boolean aPressed) {
        if (FrontIntake == null) return;

        if (yPressed) {
            FrontIntake.setPower(1.0);
        } else if (aPressed) {
            FrontIntake.setPower(-1.0);
        } else {
            FrontIntake.setPower(0.0);
        }
    }

    public void stop() {
        if (FrontIntake != null) {
            FrontIntake.setPower(0.0); // figure out if this is better than just ,setpower instead of the null statement same goes for the other 2
        }
    }
}
