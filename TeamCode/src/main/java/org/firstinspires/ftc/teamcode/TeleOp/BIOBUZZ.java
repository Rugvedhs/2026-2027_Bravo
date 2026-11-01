package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;
import org.firstinspires.ftc.teamcode.Core.ShooterRoller;
import org.firstinspires.ftc.teamcode.Core.FrontIntake;
import org.firstinspires.ftc.teamcode.Core.Pusher;
import org.firstinspires.ftc.teamcode.Core.Splitter;

@TeleOp(name="BIOBUZZ", group="TeleOp")
public class BIOBUZZ extends LinearOpMode {

    public DriveTrain driveTrain;
    public ShooterRoller ShooterRoller;
    public FrontIntake FrontIntake;
    public Pusher Pusher;
    public Splitter Splitter;

    @Override
    public void runOpMode() {

        driveTrain = new DriveTrain(hardwareMap, "frontleftdri", "backleftdri", "frontrightdri", "backrightdri");

        DcMotorEx RightShooter = getDeviceSafe(DcMotorEx.class, "RightLauncher", "RightLauncher");
        DcMotorEx LeftShooter  = getDeviceSafe(DcMotorEx.class, "Leftlauncher", "LeftLauncher");
        DcMotorEx FrontIntake  = getDeviceSafe(DcMotorEx.class, "FrontIntake", "frontintake");
        CRServo LeftPusher     = getDeviceSafe(CRServo.class, "Leftpusher", "LeftPusher");
        CRServo RightPusher    = getDeviceSafe(CRServo.class, "Rightpusher", "RightPusher");
        CRServo Splitter  = getDeviceSafe(CRServo.class, "Splitter", "splitter");

        launcherWheel = new LauncherWheel(hardwareMap.get(DcMotor.class, "LauncherWheel"));

        ShooterRoller = new ShooterRoller(RightShooter, LeftShooter);
        FrontIntake   = new FrontIntake(FrontIntake);
        Pusher        = new Pusher(LeftPusher, RightPusher);
        Splitter      = new Splitter(Splitter);

        ShooterRoller.init();
        FrontIntake.init();
        Pusher.init();
        Splitter.init();

        telemetry.addData("Status", "Initialized - BIOBUZZ TeleOp");
        telemetry.update();

        waitForStart();

        Splitter.start();

        while (opModeIsActive()) {
            driveTrain.Drive(gamepad1);


            ShooterRoller.update(gamepad2.left_stick_y, gamepad2.right_stick_y, gamepad2.b, gamepad2.x);
            FrontIntake.update(gamepad2.y, gamepad2.a);
            boolean pushRight = gamepad2.right_bumper || gamepad2.right_trigger > 0.1;
            boolean pushLeft  = gamepad2.left_bumper  || gamepad2.left_trigger > 0.1;
            Pusher.update(pushRight, pushLeft);

            telemetry.addData("Status", "Running BIOBUZZ");
            telemetry.update();
        }

        if (driveTrain != null) driveTrain.Stop();
        if (ShooterRoller != null) ShooterRoller.stop();
        if (FrontIntake != null) FrontIntake.stop();
        if (Pusher != null) Pusher.stop();
        if (Splitter != null) Splitter.stop();
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
