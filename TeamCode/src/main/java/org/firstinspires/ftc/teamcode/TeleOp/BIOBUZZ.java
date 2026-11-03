package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.ShooterRoller;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.Pusher;
import org.firstinspires.ftc.teamcode.Core.Splitter;

@TeleOp(name="BIOBUZZ", group="TeleOp")
public class BIOBUZZ extends LinearOpMode {

    public DriveTrain driveTrain;
    public ShooterRoller shooterRoller;
    public FrontIntake frontIntake;
    public Pusher pusher;
    public Splitter splitter;

    private static final float STICK_DEADZONE = 0.08f;
    private long yPressedTime = 0;

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(hardwareMap, "frontLeft", "backLeft", "frontRight", "backRight");
        pusher = new Pusher(hardwareMap.get(CRServo.class, "leftPusher"), hardwareMap.get(CRServo.class, "rightPusher"));
        shooterRoller = new ShooterRoller(hardwareMap.get(DcMotorEx.class, "leftShooterRoller"), hardwareMap.get(DcMotorEx.class, "rightShooterRoller"));
        frontIntake = new FrontIntake(hardwareMap.get(DcMotorEx.class, "frontIntake"));
        splitter = new Splitter(hardwareMap.get(CRServo.class, "splitter"));

        shooterRoller.init();
        frontIntake.init();
        pusher.init();
        splitter.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            driveTrain.Drive(gamepad1);
            splitter.start();

            float leftStick  = applyDeadzone(gamepad2.left_stick_y, STICK_DEADZONE);
            float rightStick = applyDeadzone(gamepad2.right_stick_y, STICK_DEADZONE);
            shooterRoller.update(leftStick, rightStick, gamepad2.b, gamepad2.x);
            frontIntake.update(gamepad2.y, gamepad2.a);
            boolean pushRight =  gamepad2.right_trigger > 0.1;
            boolean pushLeft  =  gamepad2.left_trigger > 0.1;
            pusher.update(pushRight, pushLeft);
            telemetry.update();
        }

        if (driveTrain != null) driveTrain.Stop();
        if (shooterRoller != null) shooterRoller.stop();
        if (frontIntake != null) frontIntake.stop();
        if (pusher != null) pusher.stop();
        if (splitter != null) splitter.stop();
    }
    private float applyDeadzone(float val, float dz) {
        return Math.abs(val) < dz ? 0.0f : val;
    }
}
