package org.firstinspires.ftc.teamcode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Test_TeleOp - Biobuzz FTC Redesign (Dual Controller System)
 *
 * DRIVER A (Gamepad 1 - Press Start + A to assign):
 * - Drivetrain Mobility & Speed Scaling
 * - Left Joystick Y / X: Forward/Backward & Turn
 * - Right Joystick X: Strafe Left/Right
 * - Right Toggle (Right Bumper): Fast Speed Mode (100%)
 * - B Button: Slow Speed Mode (40%)
 *
 * DRIVER B (Gamepad 2 - Press Start + B to assign):
 * - Mechanisms & Ball Unjamming / Sorting Controls
 * - Left Joystick Y: Left Launcher Motor (variable speed forward & reverse)
 * - Right Joystick Y: Right Launcher Motor (variable speed forward & reverse)
 * - Button B: Shoot Right Launcher at 100% full speed
 * - Button X: Shoot Left Launcher at 100% full speed
 * - Button A: Intake Motor rolls Counter-Clockwise (Intake in)
 * - Button Y: Spin Intake Motor Clockwise (eject / reverse stuck balls)
 * - Right Toggle (Right Bumper): Spin Right Pusher Servo in opposite direction (unjamming)
 * - Left Toggle (Left Bumper): Spin Left Pusher Servo in opposite direction (unjamming)
 * - Continuous Servos (Splitter & Pushers): Constantly spinning during active operation
 */
@TeleOp(name="Test_TeleOp", group="Test")
public class Test_TeleOp extends LinearOpMode {

    // ── 1. DRIVETRAIN MOTORS (4 Motors) ──────────────────────────────────
    public DcMotorEx frontLeftDrive;  // Configured name: "leftFront" (or example if not configured)
    public DcMotorEx backLeftDrive;   // Configured name: "leftbackdri" (Motor Port 3)
    public DcMotorEx frontRightDrive; // Configured name: "rightFront" (or example if not configured)
    public DcMotorEx backRightDrive;  // Configured name: "rightbackdri" (Motor Port 2)

    // ── 2. MECHANISM MOTORS (3 Motors) ───────────────────────────────────
    public DcMotorEx rightLauncher;   // Motor Port 0: "rightLauncher" (Launcher Flywheel)
    public DcMotorEx leftLauncher;    // Motor Port 1: "leftLauncher" (Launcher Flywheel)
    public DcMotor frontIntake;       // Configured name: "FrontIntake" (Top Compliant Intake)

    // ── 3. CONTINUOUS ROTATION SERVOS (3 Servos) ──────────────────────────
    public CRServo splitterServo;     // Servo Port 5: "splitterservo" (Central Belt / Conveying)
    public CRServo rightPusherServo;  // Servo Port 0: "rightpush" (Right Ball Pusher)
    public CRServo leftPusherServo;   // Servo Port 1: "leftpush" (EXAMPLE NAME: Left Ball Pusher)

    @Override
    public void runOpMode() {

        // ── HARDWARE MAP MAPPING (Safe Initialization for Testing / Prototyping) ──
        frontLeftDrive  = getDeviceSafe(DcMotorEx.class, "leftFront");
        backLeftDrive   = getDeviceSafe(DcMotorEx.class, "leftbackdri");
        frontRightDrive = getDeviceSafe(DcMotorEx.class, "rightFront");
        backRightDrive  = getDeviceSafe(DcMotorEx.class, "rightbackdri");

        rightLauncher = getDeviceSafe(DcMotorEx.class, "rightLauncher");
        leftLauncher  = getDeviceSafe(DcMotorEx.class, "leftLauncher");
        frontIntake   = getDeviceSafe(DcMotor.class, "FrontIntake");

        splitterServo    = getDeviceSafe(CRServo.class, "splitterservo");
        rightPusherServo = getDeviceSafe(CRServo.class, "rightpush");
        leftPusherServo  = getDeviceSafe(CRServo.class, "leftpush"); // EXAMPLE NAME: "leftpush" on Servo Port 1

        // ── MOTOR DIRECTIONS & ZERO-POWER BEHAVIORS ──
        // Drivetrain: BRAKE for precision, reversed left side
        if (frontLeftDrive != null) {
            frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (backLeftDrive != null) {
            backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
            backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (frontRightDrive != null) {
            frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
            frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        if (backRightDrive != null) {
            backRightDrive.setDirection(DcMotor.Direction.FORWARD);
            backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        // Launcher Flywheels: FLOAT for smooth coasting and max speed
        if (rightLauncher != null) {
            rightLauncher.setDirection(DcMotor.Direction.FORWARD);
            rightLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            rightLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        if (leftLauncher != null) {
            leftLauncher.setDirection(DcMotor.Direction.REVERSE);
            leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        // Intake Motor & Servos
        if (frontIntake != null) frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        if (splitterServo != null) splitterServo.setDirection(DcMotorSimple.Direction.REVERSE);  // CCW Conveying
        if (rightPusherServo != null) rightPusherServo.setDirection(DcMotorSimple.Direction.FORWARD); // CW Default Push
        if (leftPusherServo != null) leftPusherServo.setDirection(DcMotorSimple.Direction.FORWARD);   // CW Default Push

        // ── INITIAL TELEMETRY DISPLAY ──
        telemetry.addData("Status", "Initialized (Prototyping Safe Mode)");
        telemetry.addData("Driver A (Gamepad 1)", "Press Start+A to bind. Joysticks = Drive/Turn/Strafe | RB = Fast | B = Slow");
        telemetry.addData("Driver B (Gamepad 2)", "Press Start+B to bind. Sticks = Launchers | B/X = 100% Launchers | A/Y = Intake | RB/LB = Pusher Reverse");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER A: DRIVETRAIN CONTROLS (Gamepad 1: Start + A) ──────────
            // ──────────────────────────────────────────────────────────────────
            double forward = -gamepad1.left_stick_y;  // Forward / Backward
            double turn    =  gamepad1.left_stick_x;  // Turning
            double strafe  =  gamepad1.right_stick_x; // Strafe Left / Right

            // Speed Scaling: Fast Mode (100% on Right Bumper), Slow Mode (40% on B Button), Normal (80%)
            double driveSpeedMod = 0.80;
            if (gamepad1.right_bumper) {
                driveSpeedMod = 1.00; // Fast Mode (Max Speed)
            } else if (gamepad1.b) {
                driveSpeedMod = 0.40; // Slow Mode
            }

            // Mecanum Drive Mix
            double flPwr = (forward + strafe + turn) * driveSpeedMod;
            double blPwr = (forward - strafe + turn) * driveSpeedMod;
            double frPwr = (forward - strafe - turn) * driveSpeedMod;
            double brPwr = (forward + strafe - turn) * driveSpeedMod;

            // Normalize Motor Powers
            double maxPwr = Math.max(Math.abs(flPwr), Math.max(Math.abs(blPwr), Math.max(Math.abs(frPwr), Math.abs(brPwr))));
            if (maxPwr > 1.0) {
                flPwr /= maxPwr;
                blPwr /= maxPwr;
                frPwr /= maxPwr;
                brPwr /= maxPwr;
            }

            if (frontLeftDrive != null) frontLeftDrive.setPower(flPwr);
            if (backLeftDrive != null) backLeftDrive.setPower(blPwr);
            if (frontRightDrive != null) frontRightDrive.setPower(frPwr);
            if (backRightDrive != null) backRightDrive.setPower(brPwr);

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER B: MECHANISM CONTROLS (Gamepad 2: Start + B) ───────────
            // ──────────────────────────────────────────────────────────────────

            // 1. DUAL LAUNCHER MOTORS (Variable speed both ways + 100% Shoot buttons)
            // Joysticks push UP = Forward speed, push DOWN = Reverse speed (unjamming)
            double rightLauncherPower = -gamepad2.right_stick_y;
            double leftLauncherPower  = -gamepad2.left_stick_y;

            // Button B -> Shoot Right Launcher at 100% Full Speed
            if (gamepad2.b) {
                rightLauncherPower = 1.00;
            }

            // Button X -> Shoot Left Launcher at 100% Full Speed
            if (gamepad2.x) {
                leftLauncherPower = 1.00;
            }

            if (rightLauncher != null) rightLauncher.setPower(rightLauncherPower);
            if (leftLauncher != null) leftLauncher.setPower(leftLauncherPower);

            // 2. INTAKE MOTOR CONTROL (Spins both ways in case balls get stuck)
            // Button A -> Intake CCW (Intake In at 100% power)
            // Button Y -> Intake CW (Eject / Push Out stuck balls at 100% power)
            double intakePower = 0.0;
            if (gamepad2.a) {
                intakePower = -1.00; // Counter-Clockwise (Pull in)
            } else if (gamepad2.y) {
                intakePower = 1.00;  // Clockwise (Push out / Unjam)
            }
            if (frontIntake != null) frontIntake.setPower(intakePower);

            // 3. SPLITTER CONVEYOR SERVO (Constantly spinning at 100% speed)
            double splitterPower = -1.00; // Continuous CCW conveying
            if (splitterServo != null) splitterServo.setPower(splitterPower);

            // 4. PUSHING SERVOS (Default CW constantly spinning + Reversal for unjamming)
            // Right Toggle (Right Bumper) -> Reverse Right Pusher Servo (-1.0) while held if stuck
            double rightPushPower = gamepad2.right_bumper ? -1.00 : 1.00;

            // Left Toggle (Left Bumper) -> Reverse Left Pusher Servo (-1.0) while held if stuck
            double leftPushPower = gamepad2.left_bumper ? -1.00 : 1.00;

            if (rightPusherServo != null) rightPusherServo.setPower(rightPushPower);
            if (leftPusherServo != null) leftPusherServo.setPower(leftPushPower);

            // ──────────────────────────────────────────────────────────────────
            // ── DRIVER STATION TELEMETRY OUTPUT ──────────────────────────────
            // ──────────────────────────────────────────────────────────────────
            telemetry.addData(">> BINDING STATUS <<", "Driver A (G1: Start+A) | Driver B (G2: Start+B)");
            telemetry.addData("Driver A Speed Mode", gamepad1.right_bumper ? "FAST (100%%)" : (gamepad1.b ? "SLOW (40%%)" : "NORMAL (80%%)"));
            telemetry.addData("Driver B Launchers", "Right (Port 0): %.2f | Left (Port 1): %.2f", rightLauncherPower, leftLauncherPower);
            telemetry.addData("Driver B Intake", "Power: %.2f (A=In | Y=Out)", intakePower);
            telemetry.addData("Driver B Pushers", "Right: %.2f (RB=Unjam) | Left: %.2f (LB=Unjam)", rightPushPower, leftPushPower);
            telemetry.addData("Splitter Servo", "Power: %.2f", splitterPower);
            telemetry.update();
        }

        // Safety Stop all Motors & Servos
        if (frontLeftDrive != null) frontLeftDrive.setPower(0.0);
        if (backLeftDrive != null) backLeftDrive.setPower(0.0);
        if (frontRightDrive != null) frontRightDrive.setPower(0.0);
        if (backRightDrive != null) backRightDrive.setPower(0.0);
        if (rightLauncher != null) rightLauncher.setPower(0.0);
        if (leftLauncher != null) leftLauncher.setPower(0.0);
        if (frontIntake != null) frontIntake.setPower(0.0);
        if (splitterServo != null) splitterServo.setPower(0.0);
        if (rightPusherServo != null) rightPusherServo.setPower(0.0);
        if (leftPusherServo != null) leftPusherServo.setPower(0.0);
    }

    /**
     * Safe hardware getter for testing/prototyping:
     * Returns null instead of throwing an error if a motor/servo is not configured in the Driver Hub yet.
     */
    private <T> T getDeviceSafe(Class<T> classId, String deviceName) {
        try {
            return hardwareMap.get(classId, deviceName);
        } catch (Exception ignored) {
            return null;
        }
    }
}