package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlyWheel {

    private DcMotorEx RightFlyWheel;
    private DcMotorEx LeftFlyWheel;

    private static final double TICKS_PER_REV = 103.8; // Motor encoder resolution
    private static final double DEADZONE = 0.08;

    // Target Max RPM capped at 70% max speed (6000 RPM * 0.70 = 4200 RPM)
    public double targetRPM = 4200.0; 

    // PIDF tuning values
    double F = 0.8;
    double P = 1.0;

    public FlyWheel(DcMotorEx RightFlyWheel, DcMotorEx LeftFlyWheel) {
        this.RightFlyWheel = RightFlyWheel;
        this.LeftFlyWheel  = LeftFlyWheel;
    }

    public void init() {
        if (RightFlyWheel != null) {
            RightFlyWheel.setDirection(DcMotorSimple.Direction.REVERSE);
            RightFlyWheel.setMode(RunMode.RUN_USING_ENCODER);
            RightFlyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            RightFlyWheel.setPower(0.0);
        }
        if (LeftFlyWheel != null) {
            LeftFlyWheel.setDirection(DcMotorSimple.Direction.FORWARD);
            LeftFlyWheel.setMode(RunMode.RUN_USING_ENCODER);
            LeftFlyWheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            LeftFlyWheel.setPower(0.0);
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
        if (LeftFlyWheel != null) {
            LeftFlyWheel.setMode(RunMode.RUN_USING_ENCODER);
            LeftFlyWheel.setVelocity(ticksPerSec);
        }
    }

    public void setRightVelocity(double ticksPerSec) {
        if (RightFlyWheel != null) {
            RightFlyWheel.setMode(RunMode.RUN_USING_ENCODER);
            RightFlyWheel.setVelocity(ticksPerSec);
        }
    }

    public void stopLeft() {
        if (LeftFlyWheel != null) LeftFlyWheel.setPower(0.0);
    }

    public void stopRight() {
        if (RightFlyWheel != null) RightFlyWheel.setPower(0.0);
    }

    public void stop() {
        stopLeft();
        stopRight();
    }

    private double rpmToTicks(double rpm){
        return (rpm * TICKS_PER_REV) / 60.0;
    }

    public void updateFlywheelChanges(Telemetry telemetry){
        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);

        if (RightFlyWheel != null) RightFlyWheel.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
        if (LeftFlyWheel != null) LeftFlyWheel.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
    }

    public void getVelocityAndError(Telemetry telemetry){
        double rightVel = RightFlyWheel != null ? Math.abs(RightFlyWheel.getVelocity()) : 0.0;
        double leftVel  = LeftFlyWheel != null ? Math.abs(LeftFlyWheel.getVelocity()) : 0.0;
        double currVelocity = (rightVel + leftVel) / 2.0;
        double targetVelocity = rpmToTicks(targetRPM);

        telemetry.addData("Current Velocity", "%.1f", currVelocity);
        telemetry.addData("Target Velocity", "%.1f", targetVelocity);
        telemetry.addData("RPM Target", "%.0f", targetRPM);

        double error = targetVelocity - currVelocity;
        telemetry.addData("Error", "%.1f", error);
    }
}
