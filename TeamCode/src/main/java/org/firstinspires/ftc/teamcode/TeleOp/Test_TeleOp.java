package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;

/**
 * Test_TeleOp - Biobuzz FTC Redesign (Dual Controller System)
 *
 * DRIVER A (Gamepad 1):
 * - Right Stick Y: Drive Forward / Backward (Push UP = Forward)
 * - Right Stick X: Strafe Left / Right (Push LEFT = Strafe Left)
 * - Left Stick X: Turn Left / Right (Push LEFT = Turn Left)
 * - Right Bumper: 100% Full Speed | Button B: 40% Precision Slow Mode
 *
 * DRIVER B (Gamepad 2):
 * - Right Stick Y / Left Stick Y: Launcher Flywheels (60% Max Speed Capped, Reversed Direction)
 * - Button B / Button X: Shoot Launchers at 60% Max Speed
 * - Button A / Button Y: Intake Motor (A = In, Y = Out / Unjam)
 * - Both Pusher Servos & Splitter Servo: CONSTANTLY spinning at 100% full power in opposite direction the second TeleOp starts
 *   (Hold Right/Left Bumper to temporarily reverse pushers if unjamming)
 */
@TeleOp(name="Test_TeleOp", group="Test")
public class Test_TeleOp extends LinearOpMode {

    // ── 1. DRIVETRAIN (Core Object) ─────────────────────────────────────
    public DriveTrain driveTrain;

    // ── 2. MECHANISM MOTORS (3 Motors) ───────────────────────────────────
    public DcMotorEx rightLauncher;   // Control Hub Port 0: "rightlauncher"
    public DcMotorEx leftLauncher;    // Control Hub Port 1: "leftlauncher"
    public DcMotor frontIntake;       // Expansion Hub 2 Port 0: "frontintake"

    // ── 3. CONTINUOUS ROTATION SERVOS (3 Servos) ──────────────────────────
    public CRServo splitterServo;     // Control Hub Servo Port 5: "splitterservo"
    public CRServo rightPusherServo;  // Expansion Hub 2 Servo Port 0: "rightpush"
    public CRServo leftPusherServo;   // Control Hub Servo Port 0: "leftpush"

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

        frontIntake = getDeviceSafe(DcMotor.class, "frontintake");
        if (frontIntake == null) frontIntake = getDeviceSafe(DcMotor.class, "FrontIntake");

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
            
            // Button X -> Shoot Left Launcher at 100% Speed
            if (gamepad2.x) leftLauncherPower  = LAUNCHER_MAX_POWER;

            if (rightLauncher != null) rightLauncher.setPower(rightLauncherPower);
            if (leftLauncher != null) leftLauncher.setPower(leftLauncherPower);

            // 2. INTAKE MOTOR CONTROL
            double intakePower = 0.0;
            if (gamepad2.a) {
                intakePower = -1.00; // Counter-Clockwise (Pull in)
            } else if (gamepad2.y) {
                intakePower = 1.00;  // Clockwise (Push out / Unjam)
            }
            if (frontIntake != null) frontIntake.setPower(intakePower);

            // 3. SPLITTER CONVEYOR SERVO (Stopped by default)
            double splitterPower = 0.0;
            if (gamepad2.right_trigger > 0.1 || gamepad2.dpad_down) {
                splitterPower = -1.00; // Spin on demand
            }
            if (splitterServo != null) splitterServo.setPower(splitterPower);

            // 4. PUSHER SERVOS (Top Right Toggle on Gamepad 2)
            double rightPushPower = 0.0;
            double leftPushPower  = 0.0;

            // Top Right Toggle (Right Bumper / Right Trigger) on Gamepad 2 -> Spin rightPusherServo AND leftPusherServo Counter-Clockwise (-1.00)
            if (gamepad2.right_bumper || gamepad2.right_trigger > 0.1) {
                rightPushPower = -1.00; // The other way (-1.00)
                leftPushPower  = -1.00; // The other way (-1.00)
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
