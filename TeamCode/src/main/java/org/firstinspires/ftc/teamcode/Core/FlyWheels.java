package org.firstinspires.ftc.teamcode.Core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class FlyWheels {

    private DcMotorEx leftFly;
    private DcMotorEx rightFly;

    private static final double TICKS_PER_REV = 103.8;

    public double targetRPM = 3000;
    public double highRPM = 3500;
    public double lowRPM = 2000;

    double currTargetRPM = highRPM;

    // PIDF tuning
    double F = 0.8;
    double P = 1.0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001};
    int stepIndex = 1;

    public FlyWheels(DcMotorEx leftFly, DcMotorEx rightFly) {
        this.leftFly  = leftFly;
        this.rightFly = rightFly;
    }

    public void init() {
        if (leftFly != null) {
            leftFly.setDirection(DcMotorSimple.Direction.FORWARD);
            leftFly.setMode(RunMode.RUN_USING_ENCODER);
            leftFly.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftFly.setPower(0.0);
        }
        if (rightFly != null) {
            rightFly.setDirection(DcMotorSimple.Direction.REVERSE);
            rightFly.setMode(RunMode.RUN_USING_ENCODER);
            rightFly.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightFly.setPower(0.0);
        }
    }

    public void update(boolean rightBumper, boolean leftBumper, boolean xPressed, boolean overrideY) {

        if (xPressed || overrideY) {
            motorSpinOut();
            spinTargetRPM();
            return;
        }

        if (rightBumper) {
            motorSpinOut();
            spinTargetRPM();
        } else if (leftBumper) {
            motorSpinOut();
            spinTargetRPM();
        } else {
            stop();
        }
    }

    public void motorSpinOut(){
        if (leftFly != null) leftFly.setDirection(DcMotorSimple.Direction.REVERSE);
        if (rightFly != null) rightFly.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    public void motorSpinIn(){
        if (leftFly != null) leftFly.setDirection(DcMotorSimple.Direction.FORWARD);
        if (rightFly != null) rightFly.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void spinTargetRPM(){
        double ticksPerSec = rpmToTicks(currTargetRPM);
        if (leftFly != null) leftFly.setVelocity(ticksPerSec);
        if (rightFly != null) rightFly.setVelocity(ticksPerSec);
    }

    public void stop() {
        if (leftFly != null) leftFly.setPower(0.0);
        if (rightFly != null) rightFly.setPower(0.0);
    }

    private double rpmToTicks(double rpm){
        return (rpm * TICKS_PER_REV) / 60.0;
    }

    public void toggleVelocities(){
        if(currTargetRPM == highRPM){
            currTargetRPM = lowRPM;
        } else {
            currTargetRPM = highRPM;
        }
    }

    public void changeHighRPM(int increment){
        highRPM += increment;
    }

    public void changeHighVelocity(int increment){
        changeHighRPM(increment);
    }

    public void changeLowRPM(int increment){
        lowRPM += increment;
    }

    public void changeStepIndex(){
        stepIndex = (stepIndex + 1) % stepSizes.length;
    }

    public void incrF(){
        F += stepSizes[stepIndex];
    }

    public void decrF(){
        F -= stepSizes[stepIndex];
    }

    public void incrP(){
        P += stepSizes[stepIndex];
    }

    public void decrP(){
        P -= stepSizes[stepIndex];
    }

    public void updateFlywheelChanges(Telemetry telemetry){
        PIDFCoefficients pidf = new PIDFCoefficients(P, 0, 0, F);

        leftFly.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);
        rightFly.setPIDFCoefficients(RunMode.RUN_USING_ENCODER, pidf);

        spinTargetRPM();
    }

    public void getVelocityAndError(Telemetry telemetry){
        double leftVel = leftFly != null ? leftFly.getVelocity() : 0.0;
        double rightVel = rightFly != null ? rightFly.getVelocity() : 0.0;
        double currVelocity = (leftVel + Math.abs(rightVel)) / 2;
        double targetVelocity = rpmToTicks(currTargetRPM);

        telemetry.addData("Current Velocity", "%.1f", currVelocity);
        telemetry.addData("Target Velocity", "%.1f", targetVelocity);
        telemetry.addData("RPM Target", "%.0f", currTargetRPM);

        double error = targetVelocity - currVelocity;
        telemetry.addData("Error", "%.1f", error);

        telemetry.addData("P", "%.3f", P);
        telemetry.addData("F", "%.3f", F);
        telemetry.addData("Step Size", "%.3f", stepSizes[stepIndex]);
    }

    public void publishTelemetry(Telemetry telemetry){
        double leftVel = leftFly != null ? leftFly.getVelocity() : 0.0;
        double rightVel = rightFly != null ? rightFly.getVelocity() : 0.0;
        telemetry.addData("Left Flywheel Velocity", "%.1f", leftVel);
        telemetry.addData("Right Flywheel Velocity", "%.1f", rightVel);
    }
}