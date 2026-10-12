package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Core.Belt;
import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.FlyWheels;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.LauncherWheel;

@TeleOp(name="Biobuzz2026", group="TeleOp")
public class Biobuzz2026 extends LinearOpMode {

    public DriveTrain driveTrain;
    public LauncherWheel launcherWheel;
    public FlyWheels flyWheels;
    public FrontIntake frontIntake;
    public Belt belt;
    public CRServo pushServo;      // Configured on Servo Port 0: "rightpush"
    public CRServo splitterServo;  // Configured on Servo Port 5: "splitterservo"

    private static final double LAUNCHER_MAX_SPEED = 0.60;

    @Override
    public void runOpMode() {
        // Driver A: Drivetrain Mapping (Safe Initialization for Testing & Prototyping)
        driveTrain = new DriveTrain(
                hardwareMap,
                "leftFront", "leftbackdri",
                "rightFront", "rightbackdri"
        );

        // Driver B: Mechanism Mapping
        launcherWheel = new LauncherWheel(getDeviceSafe(DcMotor.class, "LauncherWheel"));
        frontIntake = new FrontIntake(getDeviceSafe(DcMotor.class, "FrontIntake"));
        flyWheels = new FlyWheels(
                getDeviceSafe(DcMotorEx.class, "leftLauncher"),
                getDeviceSafe(DcMotorEx.class, "rightLauncher")
        );
        belt = new Belt(getDeviceSafe(CRServo.class, "beltServo"));
        pushServo = getDeviceSafe(CRServo.class, "rightpush");
        splitterServo = getDeviceSafe(CRServo.class, "splitterservo");

        // Initialization & Directions
        if (launcherWheel != null) launcherWheel.init();
        if (flyWheels != null) flyWheels.init();
        if (frontIntake != null) frontIntake.init();
        if (belt != null) belt.init();

        if (pushServo != null) pushServo.setDirection(DcMotorSimple.Direction.REVERSE);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized (Prototyping Safe Mode) - Biobuzz2026");
        telemetry.addData("Driver A (Gamepad 1)", "Left Stick = Drive/Turn | Right Stick = Strafe | RB = Fast | B = Slow");
        telemetry.addData("Driver B (Gamepad 2)", "Sticks = Launchers | A/X/Y = Launcher Buttons | B/RB = Splitter | DPad-Up/LB/LT = Pusher");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // ── DRIVER A: DRIVETRAIN CONTROLS (Gamepad 1) ──────────────────────
            driveTrain.Drive(gamepad1);

            // ── DRIVER B: MECHANISM CONTROLS (Gamepad 2) ──────────────────────

            // 1. Dual Launchers Control (Joysticks + Face Buttons)
            double leftPwr = -gamepad2.left_stick_y * LAUNCHER_MAX_SPEED;
            double rightPwr = -gamepad2.right_stick_y * LAUNCHER_MAX_SPEED;

            if (gamepad2.a) {
                rightPwr = 0.50; // A Button -> Spin Right Launcher at 50%
            }
            if (gamepad2.x) {
                leftPwr = 0.50;  // X Button -> Spin Left Launcher at 50%
            }
            if (gamepad2.y) {
                leftPwr = 0.50;  // Y Button -> Spin Both Launchers at 50%
                rightPwr = 0.50;
            }

            flyWheels.update(
                    gamepad2.right_bumper || gamepad2.y || gamepad2.a || gamepad2.x,
                    gamepad2.left_bumper,
                    gamepad2.x,
                    gamepad2.y
            );

            // 2. Splitter Servo Control (Port 5: "splitterservo")
            double splitterPower = 0.0;
            if (gamepad2.b || gamepad2.right_bumper || gamepad2.right_trigger > 0.1) {
                splitterPower = -1.0; // 100% max speed CCW
            }
            if (splitterServo != null) splitterServo.setPower(splitterPower);

            // 3. Pushing Servo Control (Port 0: "rightpush")
            double pushPower = 0.0;
            if (gamepad2.dpad_up || gamepad2.left_bumper || gamepad2.left_trigger > 0.1) {
                pushPower = 1.0; // 100% max speed CW
            }
            if (pushServo != null) pushServo.setPower(pushPower);

            // Real-time Driver Station Telemetry Output
            telemetry.addData("Status", "Running Biobuzz TeleOp");
            telemetry.addData("Driver A Speed", gamepad1.right_bumper ? "FAST (100%%)" : (gamepad1.b ? "SLOW (60%%)" : "NORMAL (80%%)"));
            telemetry.addData("Driver B Launchers", "Left: %.2f | Right: %.2f", leftPwr, rightPwr);
            telemetry.addData("Driver B Servos", "Splitter: %.2f | Pusher: %.2f", splitterPower, pushPower);
            telemetry.update();
        }
    }

    /**
     * Safe hardware getter for prototyping/testing:
     * Returns null instead of crashing if a motor/servo is not configured in the Driver Hub yet.
     */
    private <T> T getDeviceSafe(Class<T> classId, String deviceName) {
        try {
            return hardwareMap.get(classId, deviceName);
        } catch (Exception ignored) {
            return null;
        }
    }
}
