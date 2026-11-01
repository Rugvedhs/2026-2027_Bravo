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

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(hardwareMap, "frontleftdri", "backleftdri", "frontrightdri", "backrightdri");

        DcMotorEx RightShooter = getDeviceSafe(DcMotorEx.class, "RightLauncher", "RightLauncher");
        DcMotorEx LeftShooter  = getDeviceSafe(DcMotorEx.class, "Leftlauncher", "LeftLauncher");
        DcMotorEx FrontIntake  = getDeviceSafe(DcMotorEx.class, "FrontIntake", "frontintake");
        CRServo LeftPusher     = getDeviceSafe(CRServo.class, "Leftpusher", "LeftPusher");
        CRServo RightPusher    = getDeviceSafe(CRServo.class, "Rightpusher", "RightPusher");
        CRServo Splitter  = getDeviceSafe(CRServo.class, "Splitter", "splitter");

     //   launcherWheel = new LauncherWheel(hardwareMap.get(DcMotor.class, "LauncherWheel"));

        shooterRoller = new ShooterRoller(RightShooter, LeftShooter);
        frontIntake   = new FrontIntake(FrontIntake);
        pusher        = new Pusher(LeftPusher, RightPusher);
        splitter      = new Splitter(Splitter);

        shooterRoller.init();
        frontIntake.init();
        pusher.init();
        splitter.init();

        telemetry.addData("Status", "Initialized - BIOBUZZ TeleOp");
        telemetry.update();

        waitForStart();

        splitter.start();

        while (opModeIsActive()) {
            driveTrain.Drive(gamepad1);


            shooterRoller.update(gamepad2.left_stick_y, gamepad2.right_stick_y, gamepad2.b, gamepad2.x);
            frontIntake.update(gamepad2.y, gamepad2.a);
            boolean pushRight = gamepad2.right_bumper || gamepad2.right_trigger > 0.1;
            boolean pushLeft  = gamepad2.left_bumper  || gamepad2.left_trigger > 0.1;
            pusher.update(pushRight, pushLeft);

            telemetry.addData("Status", "Running BIOBUZZ");
            telemetry.update();
        }

        if (driveTrain != null) driveTrain.Stop();
        if (shooterRoller != null) shooterRoller.stop();
        if (frontIntake != null) frontIntake.stop();
        if (pusher != null) pusher.stop();
        if (splitter != null) splitter.stop();
    }

    private <T> T getDeviceSafe(Class<T> classId, String primaryName, String... fallbacks) {
        if (primaryName != null) {
            try { return hardwareMap.get(classId, primaryName); } catch (Exception ignored) {}
        }
        for (String name : fallbacks) {
            try { return hardwareMap.get(classId, name); } catch (Exception ignored) {}
        }
        return null;
    }
}
