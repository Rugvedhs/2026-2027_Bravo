package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;

@TeleOp(name="BIOBUZZ", group="TeleOp")
public class BIOBUZZ extends LinearOpMode {
    public DriveTrain driveTrain;
    public DcMotorEx rightLauncher;
    public DcMotorEx leftLauncher;
    public DcMotorEx frontIntake;
    public CRServo splitterServo;
    public CRServo rightPusherServo;
    public CRServo leftPusherServo;

    // Launcher Flywheel Speed (70% Speed)
    private static final double LAUNCHER_MAX_POWER = 0.70;

    @Override
    public void runOpMode() {

        // ── HARDWARE INITIALIZATION ──
        driveTrain = new DriveTrain(
                hardwareMap,
                "frontleftdri", "backleftdri",
                "frontrightdri", "backrightdri"
        );

        rightLauncher = getDeviceSafe(DcMotorEx.class, "rightlauncher");
        if (rightLauncher == null) rightLauncher = getDeviceSafe(DcMotorEx.class, "rightLauncher");

        leftLauncher = getDeviceSafe(DcMotorEx.class, "leftlauncher");
        if (leftLauncher == null) leftLauncher = getDeviceSafe(DcMotorEx.class, "leftLauncher");

        frontIntake = getDeviceSafe(DcMotorEx.class, "frontintake");
        if (frontIntake == null) frontIntake = getDeviceSafe(DcMotorEx.class, "FrontIntake");

        splitterServo    = getDeviceSafe(CRServo.class, "splitterservo"); // CH Servo Port 5
        rightPusherServo = getDeviceSafe(CRServo.class, "rightpush");     // EH2 Servo Port 0
        leftPusherServo  = getDeviceSafe(CRServo.class, "leftpush");      // CH Servo Port 0

        // Launcher Flywheels: Reversed Directions + FLOAT for smooth coasting
        if (rightLauncher != null) {
            rightLauncher.setDirection(DcMotor.Direction.REVERSE); // Reversed as requested
            rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (leftLauncher != null) {
            leftLauncher.setDirection(DcMotor.Direction.FORWARD); // Reversed as requested
            leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // Intake Motor & Servos
        if (frontIntake != null) frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);  // CCW Conveying
        
        // Pusher Servos: Right Pusher set to FORWARD for continuous Clockwise (CW) spin at full power
        if (rightPusherServo != null) rightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD); // Clockwise (CW)
        if (leftPusherServo != null) leftPusherServo.setDirection(DcMotorSimple.Direction.REVERSE);   // Counter-Clockwise (CCW)

        // ── INITIAL TELEMETRY DISPLAY ──
        telemetry.addData("Status", "Initialized - Test_TeleOp (70%% Launchers | Continuous Pushers)");
        telemetry.addData("Driver A (Gamepad 1)", "RS Y = Forward/Back | RS X = Strafe | LS X = Turn | B = Slow Mode");
        telemetry.addData("Driver B (Gamepad 2)", "Sticks = Launchers (70%% Max) | A/Y = Intake | Pushers & Splitter = CONSTANT 100%%");
        telemetry.update();

        waitForStart();

        boolean isLeftLauncherOn = false;
        boolean x2PreviouslyPressed = false;

        while (opModeIsActive()) {

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER A: DRIVETRAIN CONTROLS (Gamepad 1) ─────────────────────
            // ──────────────────────────────────────────────────────────────────
            driveTrain.Drive(gamepad1);

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER B: MECHANISM CONTROLS (Gamepad 2) ──────────────────────
            // ──────────────────────────────────────────────────────────────────

            // 1. DUAL LAUNCHER MOTORS (70% Speed + Reversed Direction)
            double rightLauncherPower = -gamepad2.right_stick_y * LAUNCHER_MAX_POWER;
            double leftLauncherPower  = -gamepad2.left_stick_y * LAUNCHER_MAX_POWER;

            // Button B -> Shoot Right Launcher at 70% Speed
            if (gamepad2.b) rightLauncherPower = LAUNCHER_MAX_POWER;
            
            // Button X (Gamepad 2) -> Press-to-Toggle Left Launcher ON / OFF until pressed again
            boolean x2Pressed = gamepad2.x;
            if (x2Pressed && !x2PreviouslyPressed) {
                isLeftLauncherOn = !isLeftLauncherOn;
            }
            x2PreviouslyPressed = x2Pressed;

            if (isLeftLauncherOn) {
                leftLauncherPower = LAUNCHER_MAX_POWER; // Same speed as before (70% power)
            }

            if (rightLauncher != null) rightLauncher.setPower(rightLauncherPower);
            if (leftLauncher != null) leftLauncher.setPower(leftLauncherPower);

            // 2. INTAKE MOTOR CONTROL (Button Y = Intaking, Button A = Outtaking)
            double intakePower = 0.0;
            if (gamepad2.y) {
                intakePower = 1.00;  // Button Y = Intaking
            } else if (gamepad2.a) {
                intakePower = -1.00; // Button A = Outtaking
            }
            if (frontIntake != null) frontIntake.setPower(intakePower);

            // 3. SPLITTER CONVEYOR SERVO (Stopped by default)
            double splitterPower = 0.0;
            if (gamepad2.right_trigger > 0.1 || gamepad2.dpad_down) {
                splitterPower = -1.00; // Spin on demand
            }
            if (splitterServo != null) splitterServo.setPower(splitterPower);

            // 4. PUSHER SERVOS (Right Bumper on Gamepad 2)
            double rightPushPower = 0.0;
            double leftPushPower  = 0.0;

            // Right Bumper / Right Trigger on Gamepad 2 -> Other direction (-1.00)
            if (gamepad2.right_bumper || gamepad2.right_trigger > 0.1) {
                rightPushPower = -1.00;
                leftPushPower  = -1.00;
            }

            if (rightPusherServo != null) rightPusherServo.setPower(rightPushPower);
            if (leftPusherServo != null) leftPusherServo.setPower(leftPushPower);

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER STATION TELEMETRY OUTPUT ──────────────────────────────
            // ──────────────────────────────────────────────────────────────────
            telemetry.addData(">> STATUS <<", "Running Test_TeleOp");
            telemetry.addData("Driver A Speed", gamepad1.b ? "SLOW (40%%)" : "FULL POWER (100%%)");
            telemetry.addData("Driver B Launchers (60%% Cap)", "Right: %.2f | Left: %.2f", rightLauncherPower, leftLauncherPower);
            telemetry.addData("Driver B Pushers (Constant)", "Right: %.2f | Left: %.2f", rightPushPower, leftPushPower);
            telemetry.addData("Driver B Intake", "Power: %.2f", intakePower);
            telemetry.update();
        }

        // Safety Stop
        if (driveTrain != null) driveTrain.Stop();
        if (rightLauncher != null) rightLauncher.setPower(0.0);
        if (leftLauncher != null) leftLauncher.setPower(0.0);
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
