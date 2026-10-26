package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * RedShootingHive1 - Preloaded with 4 Artifacts
 * Position: Red Alliance Side - Hive 1 Base (Position 1)
 * Total Execution Time: ~27 Seconds
 */
@Autonomous(name="redshootinghive1", group="Autonomous")
public class RedShootingHive1 extends LinearOpMode {

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
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);  // CCW
        if (rightPusherServo != null) rightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD); // CW Default
        if (leftPusherServo != null) leftPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);   // CW Default

        telemetry.addData("Status", "Initialized - redshootinghive1");
        telemetry.addData("Preload", "4 Artifacts Loaded");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Status", "1. Driving 1 Tile Forward at Max Speed");
            telemetry.update();
            driver.forward_tiles(1.0, 1.0);

            telemetry.addData("Status", "2. Intaking & Shooting Preloaded Artifacts");
            telemetry.update();
            startShootingMechanisms();
            sleep(3500);

            telemetry.addData("Status", "3. Strafing 2.3 Tiles Left & Turning 90 CCW");
            telemetry.update();
            driver.strafe_tiles(-2.3, 1.0);
            driver.turn_ticks(-935, 1.0); // 90 CCW

            telemetry.addData("Status", "4. Driving Forward & Intaking 4 Artifacts");
            telemetry.update();
            driver.forward_tiles(1.0, 1.0);
            if (frontIntake != null) frontIntake.setPower(1.0);
            sleep(2500);

            telemetry.addData("Status", "5. Backing 1 Tile, Turning 270 CCW & Strafing 0.5");
            telemetry.update();
            driver.forward_tiles(-1.0, 1.0);
            driver.turn_ticks(-2805, 1.0); // 270 CCW
            driver.strafe_tiles(0.5, 1.0);

            telemetry.addData("Status", "6. Shooting Intaked Artifacts");
            telemetry.update();
            startShootingMechanisms();
            sleep(3500);

            telemetry.addData("Status", "7. Strafing 2.3 Left, Forward 3.5 & Strafe -0.5 Left");
            telemetry.update();
            driver.strafe_tiles(-2.3, 1.0);
            driver.forward_tiles(3.5, 1.0);
            driver.strafe_tiles(-0.5, 1.0);
        }

        stopAllMechanisms();
    }

    private void startShootingMechanisms() {
        if (leftLauncher != null) leftLauncher.setPower(1.0);
        if (rightLauncher != null) rightLauncher.setPower(1.0);
        if (launcherWheel != null) launcherWheel.setPower(1.0);
        if (frontIntake != null) frontIntake.setPower(1.0);
        if (splitterServo != null) splitterServo.setPower(-1.0);
        if (leftPusherServo != null) leftPusherServo.setPower(1.0);
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
