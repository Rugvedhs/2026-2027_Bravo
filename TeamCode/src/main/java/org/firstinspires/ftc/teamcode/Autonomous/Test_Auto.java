package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Test_Auto - Biobuzz Autonomous Shooting & Sorting Routine
 */
@Autonomous(name="Test_Auto", group="Autonomous")
public class Test_Auto extends LinearOpMode {

    public Driver driver;
    public DcMotor leftLauncher;    // Motor Port 1: "leftLauncher"
    public DcMotor rightLauncher;   // Motor Port 0: "rightLauncher"
    public DcMotor launcherWheel;   // Feeder Wheel: "LauncherWheel"
    public DcMotor frontIntake;     // Front Intake: "FrontIntake"
    public CRServo beltServo;       // Transfer Belt: "beltServo"
    public CRServo pushServo;       // Pushing Servo Port 0: "rightpush"
    public CRServo splitterServo;   // Splitter Servo Port 5: "splitterservo"

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        // Safe Hardware Mappings for Testing & Prototyping
        leftLauncher  = getDeviceSafe(DcMotor.class, "leftLauncher");
        rightLauncher = getDeviceSafe(DcMotor.class, "rightLauncher");
        launcherWheel = getDeviceSafe(DcMotor.class, "LauncherWheel");
        frontIntake   = getDeviceSafe(DcMotor.class, "FrontIntake");
        beltServo     = getDeviceSafe(CRServo.class, "beltServo");
        pushServo     = getDeviceSafe(CRServo.class, "rightpush");
        splitterServo = getDeviceSafe(CRServo.class, "splitterservo");

        // Set Motor & Servo Directions
        if (leftLauncher != null) leftLauncher.setDirection(DcMotorSimple.Direction.REVERSE);
        if (rightLauncher != null) rightLauncher.setDirection(DcMotorSimple.Direction.FORWARD);
        if (pushServo != null) pushServo.setDirection(DcMotorSimple.Direction.FORWARD);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized - Test_Auto Ready");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // 1. Move into shooting position
            telemetry.addData("Phase", "1. Position Drive");
            telemetry.update();
            driver.forward_tiles(-0.4, 0.5);

            // 2. Spin up Launchers & Splitter System
            telemetry.addData("Phase", "2. Spinning Up Launchers");
            telemetry.update();
            if (leftLauncher != null) leftLauncher.setPower(0.85);
            if (rightLauncher != null) rightLauncher.setPower(0.85);
            if (splitterServo != null) splitterServo.setPower(-1.0);
            sleep(1500);

            // 3. Launch preloaded element
            telemetry.addData("Phase", "3. Shooting Preloaded Artifact");
            telemetry.update();
            if (launcherWheel != null) launcherWheel.setPower(1.0);
            if (pushServo != null) pushServo.setPower(1.0);
            sleep(2500);

            // 4. Activate Front Intake & Transfer Conveyor
            telemetry.addData("Phase", "4. Intaking & Conveying Transfer");
            telemetry.update();
            if (frontIntake != null) frontIntake.setPower(1.0);
            if (beltServo != null) beltServo.setPower(1.0);
            sleep(3000);

            // 5. Clean Stop All Subsystems
            telemetry.addData("Phase", "5. Stopping Mechanisms");
            telemetry.update();
            stopAllMechanisms();

            // 6. Park in Leave / Observation Zone
            telemetry.addData("Phase", "6. Parking / Leaving");
            telemetry.update();
            driver.turn_ticks(390, 0.5);
            driver.forward_tiles(0.4, 0.5);
            driver.strafe_tiles(-0.8, 0.5);

            telemetry.addData("Status", "Autonomous Complete!");
            telemetry.update();
        }
    }

    private void stopAllMechanisms() {
        if (leftLauncher != null) leftLauncher.setPower(0.0);
        if (rightLauncher != null) rightLauncher.setPower(0.0);
        if (launcherWheel != null) launcherWheel.setPower(0.0);
        if (frontIntake != null) frontIntake.setPower(0.0);
        if (beltServo != null) beltServo.setPower(0.0);
        if (pushServo != null) pushServo.setPower(0.0);
        if (splitterServo != null) splitterServo.setPower(0.0);
    }

    private <T> T getDeviceSafe(Class<T> classId, String deviceName) {
        try {
            return hardwareMap.get(classId, deviceName);
        } catch (Exception ignored) {
            return null;
        }
    }
}