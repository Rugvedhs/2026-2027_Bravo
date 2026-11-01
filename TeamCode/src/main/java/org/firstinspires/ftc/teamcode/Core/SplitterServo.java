package org.firstinspires.ftc.teamcode.Core;
import com.qualcomm.robotcore.hardware.CRServo;

public class SplitterServo {
    private final CRServo SplitterServo;
    private boolean isRunning = false;
    public SplitterServo(CRServo SplitterServo) {
        this.SplitterServo = SplitterServo;
    }
    public void init() {
        if (SplitterServo != null) {
            SplitterServo.setDirection(CRServo.Direction.FORWARD);
            SplitterServo.setPower(0.0);
        }
        isRunning = false;
    }
    public void start() {
        if (SplitterServo != null) {
            SplitterServo.setPower(1.0);
        }
        isRunning = true;
    }

    public void stop() {
        if (SplitterServo != null) {
            SplitterServo.setPower(0.0);
        }
        isRunning = false;
    }
    public void setPower(double power) {
        if (SplitterServo != null) {
            SplitterServo.setPower(power);
        }
        isRunning = (power != 0.0);
    }

    public boolean isRunning() {
        return isRunning;
    }
}
