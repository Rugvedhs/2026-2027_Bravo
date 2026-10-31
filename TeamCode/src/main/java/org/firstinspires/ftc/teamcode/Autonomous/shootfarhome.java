package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * shootfarhome Autonomous OpMode:
 * 1. Move forward 1.25 tiles.
 * 2. Spin intake & launchers to shoot 4 balls.
 * 3. Strafe 1 tile left.
 * 4. Drive 4 tiles forward.
 * 5. Strafe 1 tile left.
 */
@Autonomous(name="shootfarhome", group="Autonomous")
public class shootfarhome extends LinearOpMode {

    private Driver driver;
    private DcMotorEx rightLauncher;
    private DcMotorEx leftLauncher;
    private DcMotor frontIntake;
    private CRServo splitterServo;
    private CRServo rightPusherServo;
    private CRServo leftPusherServo;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        rightLauncher = getDeviceSafe(DcMotorEx.class, "rightlauncher");
        if (rightLauncher == null) rightLauncher = getDeviceSafe(DcMotorEx.class, "rightLauncher");

        leftLauncher = getDeviceSafe(DcMotorEx.class, "leftlauncher");
        if (leftLauncher == null) leftLauncher = getDeviceSafe(DcMotorEx.class, "leftLauncher");

        frontIntake = getDeviceSafe(DcMotor.class, "frontintake");
        if (frontIntake == null) frontIntake = getDeviceSafe(DcMotor.class, "FrontIntake");

        splitterServo    = getDeviceSafe(CRServo.class, "splitterservo");
        rightPusherServo = getDeviceSafe(CRServo.class, "rightpush");
        leftPusherServo  = getDeviceSafe(CRServo.class, "leftpush");

        if (rightLauncher != null) {
            rightLauncher.setDirection(DcMotor.Direction.REVERSE);
            rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (leftLauncher != null) {
            leftLauncher.setDirection(DcMotor.Direction.FORWARD);
            leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (frontIntake != null) frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);
        if (rightPusherServo != null) rightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);
        if (leftPusherServo != null) leftPusherServo.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized - shootfarhome");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // 1. Move forward 1.25 tiles
            telemetry.addData("Step", "1. Driving Forward 1.25 Tiles");
            telemetry.update();
            driver.forward_tiles(1.25, 0.5);

            // 2. Power ON launchers, intake, splitter, and pushers to shoot 4 balls
            telemetry.addData("Step", "2. Spinning Launchers & Intake to Shoot 4 Balls");
            telemetry.update();

            if (rightLauncher != null) rightLauncher.setPower(0.70);
            if (leftLauncher != null) leftLauncher.setPower(0.70);
            sleep(1000); // Allow flywheels to reach full RPM

            if (frontIntake != null) frontIntake.setPower(-1.0);
            if (splitterServo != null) splitterServo.setPower(-1.0);
            if (rightPusherServo != null) rightPusherServo.setPower(1.0);
            if (leftPusherServo != null) leftPusherServo.setPower(1.0);

            sleep(3500); // Shoot all 4 balls

            // Stop mechanism motors
            if (rightLauncher != null) rightLauncher.setPower(0.0);
            if (leftLauncher != null) leftLauncher.setPower(0.0);
            if (frontIntake != null) frontIntake.setPower(0.0);
            if (splitterServo != null) splitterServo.setPower(0.0);
            if (rightPusherServo != null) rightPusherServo.setPower(0.0);
            if (leftPusherServo != null) leftPusherServo.setPower(0.0);

            // 3. Strafe 1 tile left
            telemetry.addData("Step", "3. Strafing 1 Tile Left");
            telemetry.update();
            driver.strafe_tiles(-1.0, 0.5);

            // 4. Drive 4 tiles forward
            telemetry.addData("Step", "4. Driving 4 Tiles Forward");
            telemetry.update();
            driver.forward_tiles(4.0, 0.6);

            // 5. Strafe 1 tile left
            telemetry.addData("Step", "5. Strafing 1 Tile Left");
            telemetry.update();
            driver.strafe_tiles(-1.0, 0.5);

            telemetry.addData("Step", "Autonomous Complete - Parked Home");
            telemetry.update();
        }
    }

    private <T> T getDeviceSafe(Class<T> classId, String deviceName) {
        try {
            return hardwareMap.get(classId, deviceName);
        } catch (Exception ignored) {
            return null;
        }
    }
}
