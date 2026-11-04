package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class ShooterRoller {

    private DcMotorEx RightShooterRoller;
    private DcMotorEx LeftShooterRoller;

    private static final double TICKS_PER_REV = 103.8; // Motor encoder resolution
    private static final double DEADZONE = 0.08;

    // Target Max RPM capped at 70% max speed (6000 RPM * 0.70 = 4200 RPM)
    public double targetRPM = -4200.0;

    // PIDF tuning values
    double F = 0.8;
    double P = 1.0;

    public ShooterRoller(DcMotorEx RightShooterRoller, DcMotorEx LeftShooterRoller) {
        this.RightShooterRoller = RightShooterRoller;
        this.LeftShooterRoller  = LeftShooterRoller;
    }

    public void init() {
        if (RightShooterRoller != null) {
            RightShooterRoller.setDirection(DcMotorEx.Direction.REVERSE);
            RightShooterRoller.setMode(RunMode.RUN_USING_ENCODER);
            RightShooterRoller.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
            RightShooterRoller.setPower(0.0);
        }
        if (LeftShooterRoller != null) {
            LeftShooterRoller.setDirection(DcMotorEx.Direction.FORWARD);
            LeftShooterRoller.setMode(RunMode.RUN_USING_ENCODER);
            LeftShooterRoller.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
            LeftShooterRoller.setPower(0.0);
        }
    }

    /**
     * Natural Analog Joystick Control:
     * - Joystick UP/DOWN: Direct 1:1 raw analog stick input (just like drivetrain joysticks)
     * - Button B / Button X: Full 70% Max RPM Override
     */
    public void update(double leftStickY, double rightStickY, boolean bPressed, boolean xPressed) {
        double maxVelocityTicks = rpmToTicks(targetRPM);

        // Left Flywheel: Direct raw analog stick Y (-leftStickY)
        double leftInput = -leftStickY; 
        if (Math.abs(leftInput) < DEADZONE) {
            leftInput = 0.0;
        }

        if (xPressed) {
            setLeftVelocity(maxVelocityTicks); // Button X = Full 70% Speed
        } else if (leftInput != 0.0) {
            setLeftVelocity(leftInput * maxVelocityTicks); // Direct natural analog joystick scaling
        } else {
            stopLeft();
        }

        // Right Flywheel: Direct raw analog stick Y (-rightStickY)
        double rightInput = -rightStickY; 
        if (Math.abs(rightInput) < DEADZONE) {
            rightInput = 0.0;
        }

        if (bPressed) {
            setRightVelocity(maxVelocityTicks); // Button B = Full 70% Speed
        } else if (rightInput != 0.0) {
            setRightVelocity(rightInput * maxVelocityTicks); // Direct natural analog joystick scaling
        } else {
            stopRight();
        }
    }

    public void setLeftVelocity(double ticksPerSec) {
        if (LeftShooterRoller != null) {
            LeftShooterRoller.setMode(RunMode.RUN_USING_ENCODER);
            LeftShooterRoller.setVelocity(ticksPerSec);
        }
    }

    public void setRightVelocity(double ticksPerSec) {
        if (RightShooterRoller != null) {
            RightShooterRoller.setMode(RunMode.RUN_USING_ENCODER);
            RightShooterRoller.setVelocity(ticksPerSec);
        }
    }

    public void stopLeft() {
        if (LeftShooterRoller != null) LeftShooterRoller.setPower(0.0);
    }

    public void stopRight() {
        if (RightShooterRoller != null) RightShooterRoller.setPower(0.0);
    }

    public void stop() {
        stopLeft();
        stopRight();
    }

    private double rpmToTicks(double rpm){
        return (rpm * TICKS_PER_REV) / 60.0;
    }

    public void updateShooterRollerChanges(Telemetry telemetry){
        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);

        if (RightShooterRoller != null) RightShooterRoller.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
        if (LeftShooterRoller != null) LeftShooterRoller.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
    }
}
