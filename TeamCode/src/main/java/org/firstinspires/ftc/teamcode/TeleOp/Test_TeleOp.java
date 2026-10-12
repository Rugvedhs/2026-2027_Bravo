package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Test_TeleOp", group="Test")
public class Test_TeleOp extends LinearOpMode {

    public DcMotorEx rightLauncher; // Configured on Motor Port 0: "rightLauncher"
    public DcMotorEx leftLauncher;  // Configured on Motor Port 1: "leftLauncher"
    public CRServo splitterServo;   // Configured on Servo Port 5: "splitterservo"
    public CRServo pushServo;       // Configured on Servo Port 0: "rightpush"

    // Caps motor speed at 60%
    private static final double MAX_SPEED = 0.60;

    @Override
    public void runOpMode() {
        // Hardware Map mapping
        rightLauncher = hardwareMap.get(DcMotorEx.class, "rightLauncher");
        leftLauncher  = hardwareMap.get(DcMotorEx.class, "leftLauncher");
        splitterServo = hardwareMap.get(CRServo.class, "splitterservo");
        pushServo     = hardwareMap.get(CRServo.class, "rightpush");

        // Set directions and zero-power float behaviors
        rightLauncher.setDirection(DcMotor.Direction.FORWARD);
        leftLauncher.setDirection(DcMotor.Direction.REVERSE);
        splitterServo.setDirection(DcMotorSimple.Direction.REVERSE); // Counter-Clockwise rotation
        pushServo.setDirection(DcMotorSimple.Direction.REVERSE);     // Reversed rotation direction (100% Full Speed)

        rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rightLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status", "Initialized - Press PLAY to Start");
        telemetry.addData("Motor Port 0", "rightLauncher");
        telemetry.addData("Motor Port 1", "leftLauncher");
        telemetry.addData("Servo Port 0", "rightpush (D-Pad UP / LT)");
        telemetry.addData("Servo Port 5", "splitterservo (B Button)");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // Gamepad 1 Joystick Controls (Push stick UP)
            double leftPower  = -gamepad1.left_stick_y * MAX_SPEED;
            double rightPower = -gamepad1.right_stick_y * MAX_SPEED;

            // Direct Button Overrides for quick button testing:
            // A Button -> Spin Right Launcher (Port 0) at 50%
            // X Button -> Spin Left Launcher (Port 1) at 50%
            // Y Button -> Spin Both Launchers at 50%
            if (gamepad1.a) {
                rightPower = 0.50;
            }
            if (gamepad1.x) {
                leftPower = 0.50;
            }
            if (gamepad1.y) {
                leftPower = 0.50;
                rightPower = 0.50;
            }

            // Splitter Servo Control (B Button or Right Bumper spins CCW at 100% full max speed)
            double splitterPower = 0.0;
            if (gamepad1.b || gamepad1.right_bumper || gamepad1.right_trigger > 0.1) {
                splitterPower = -1.0; // 100% max speed counter-clockwise
            }

            // Pushing Servo Control (D-Pad UP, Left Bumper, or Left Trigger spins REVERSED at 100% full speed)
            double pushPower = 0.0;
            if (gamepad1.dpad_up || gamepad1.left_bumper || gamepad1.left_trigger > 0.1) {
                pushPower = 1.0; // 100% max speed reversed
            }

            leftLauncher.setPower(leftPower);
            rightLauncher.setPower(rightPower);
            splitterServo.setPower(splitterPower);
            pushServo.setPower(pushPower);

            // Real-time telemetry output on Driver Station
            telemetry.addData(">> CONTROLLER BIND NOTICE <<", "Press Start+A on controller to assign Gamepad 1!");
            telemetry.addData("Gamepad 1 Sticks", "Left Y: %.2f | Right Y: %.2f", gamepad1.left_stick_y, gamepad1.right_stick_y);
            telemetry.addData("Gamepad 1 Buttons", "A: %b | X: %b | Y: %b | B: %b | DPad-Up: %b", gamepad1.a, gamepad1.x, gamepad1.y, gamepad1.b, gamepad1.dpad_up);
            telemetry.addData("Left Launcher (Motor 1)", "Power: %.2f", leftPower);
            telemetry.addData("Right Launcher (Motor 0)", "Power: %.2f", rightPower);
            telemetry.addData("Splitter Servo (Servo 5)", "Power: %.2f (CCW)", splitterPower);
            telemetry.addData("Pushing Servo (Servo 0)", "Power: %.2f (CW - Push Up)", pushPower);
            telemetry.update();
        }

        // Safety stop
        leftLauncher.setPower(0.0);
        rightLauncher.setPower(0.0);
        splitterServo.setPower(0.0);
        pushServo.setPower(0.0);
    }
}