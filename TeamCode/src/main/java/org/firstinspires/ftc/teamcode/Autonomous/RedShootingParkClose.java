package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * RedShootingParkClose - Preloaded with 4 Artifacts
 * Position: Red Alliance Side - Position 2 (Park Close)
 * Total Execution Time: ~12 Seconds
 */
@Autonomous(name="redshootingparkclose", group="Autonomous")
public class RedShootingParkClose extends LinearOpMode {

    // Driver & Drivetrain Helper
    public Driver driver;

    // Mechanism Motors
    public DcMotorEx rightLauncher;  // Motor Port 0: "rightLauncher"
    public DcMotorEx leftLauncher;   // Motor Port 1: "leftLauncher"
    public DcMotor launcherWheel;    // Feeder Wheel: "LauncherWheel"
    public DcMotor frontIntake;      // Front Intake: "FrontIntake"

    // Continuous Rotation Servos
    public CRServo splitterServo;    // Servo Port 5: "splitterservo"
    public CRServo rightPusherServo; // Servo Port 0: "rightpush"
    public CRServo leftPusherServo;  // Servo Port 1 (Example): "leftpush"

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        rightLauncher    = getDeviceSafe(DcMotorEx.class, "rightLauncher");
        leftLauncher     = getDeviceSafe(DcMotorEx.class, "leftLauncher");
        launcherWheel    = getDeviceSafe(DcMotor.class, "LauncherWheel");
        frontIntake      = getDeviceSafe(DcMotor.class, "FrontIntake");
        splitterServo    = getDeviceSafe(CRServo.class, "splitterservo");
        rightPusherServo = getDeviceSafe(CRServo.class, "rightpush");
        leftPusherServo  = getDeviceSafe(CRServo.class, "leftpush");

        if (rightLauncher != null) {
            rightLauncher.setDirection(DcMotor.Direction.FORWARD);
            rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        if (leftLauncher != null) {
            leftLauncher.setDirection(DcMotor.Direction.REVERSE);
            leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }
        if (frontIntake != null) frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);
        if (rightPusherServo != null) rightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);
        if (leftPusherServo != null) leftPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);

        telemetry.addData("Status", "Initialized - redshootingparkclose");
        telemetry.addData("Preload", "4 Artifacts Loaded");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Status", "1. Driving 1.5 Tiles Forward");
            telemetry.update();
            driver.forward_tiles(1.5, 1.0);

            telemetry.addData("Status", "2. Strafing 3.0 Tiles Right to Park Close");
            telemetry.update();
            driver.strafe_tiles(3.0, 1.0);

            telemetry.addData("Status", "Park Close Complete!");
            telemetry.update();
        }

        stopAllMechanisms();
    }

    private void stopAllMechanisms() {
        if (rightLauncher != null) rightLauncher.setPower(0.0);
        if (leftLauncher != null) leftLauncher.setPower(0.0);
        if (launcherWheel != null) launcherWheel.setPower(0.0);
        if (frontIntake != null) frontIntake.setPower(0.0);
        if (splitterServo != null) splitterServo.setPower(0.0);
        if (rightPusherServo != null) rightPusherServo.setPower(0.0);
        if (leftPusherServo != null) leftPusherServo.setPower(0.0);
    }

    private <T> T getDeviceSafe(Class<T> classId, String deviceName) {
        try {
            return hardwareMap.get(classId, deviceName);
        } catch (Exception ignored) {
            return null;
        }
    }
}
