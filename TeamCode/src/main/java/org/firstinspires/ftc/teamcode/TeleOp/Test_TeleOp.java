package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name="Test_TeleOp", group="Test")
public class Test_TeleOp extends LinearOpMode {

    public DcMotorEx rightLauncher; // Configured on Port 0: "rightlauncher"
    public DcMotorEx leftLauncher;  // Configured on Port 1: "leftlauncher"

    // Caps motor speed at 30%
    private static final double MAX_SPEED = 0.30;

    @Override
    public void runOpMode() {
        // Hardware Map mapping
        rightLauncher = hardwareMap.get(DcMotorEx.class, "rightLauncher");
        leftLauncher  = hardwareMap.get(DcMotorEx.class, "leftLauncher");

        // Set directions and zero-power float behaviors
        rightLauncher.setDirection(DcMotor.Direction.FORWARD);
        leftLauncher.setDirection(DcMotor.Direction.REVERSE);

        rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rightLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized - Test_TeleOp");
        telemetry.addData("Port 0", "rightlauncher (Right Stick)");
        telemetry.addData("Port 1", "leftlauncher (Left Stick)");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Gamepad 1 Controls
            // Left Stick Y  -> Left Launcher (Port 1) at 30% speed
            // Right Stick Y -> Right Launcher (Port 0) at 30% speed
            double leftPower  = -gamepad1.left_stick_y * MAX_SPEED;
            double rightPower = -gamepad1.right_stick_y * MAX_SPEED;

            leftLauncher.setPower(leftPower);
            rightLauncher.setPower(rightPower);

            // Real-time telemetry output on Driver Station
            telemetry.addData("Left Launcher (Port 1)", "Power: %.2f | Pos: %d", leftPower, leftLauncher.getCurrentPosition());
            telemetry.addData("Right Launcher (Port 0)", "Power: %.2f | Pos: %d", rightPower, rightLauncher.getCurrentPosition());
            telemetry.update();
        }

        // Safety stop
        leftLauncher.setPower(0.0);
        rightLauncher.setPower(0.0);
    }
}