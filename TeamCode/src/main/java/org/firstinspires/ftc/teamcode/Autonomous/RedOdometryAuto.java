package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Stuff.MecanumDrive;

@Autonomous(name = "RedOdometryAuto")
public class RedOdometryAuto extends LinearOpMode {

    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    @Override
    public void runOpMode() throws InterruptedException {

        launcherWheel = hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake = hardwareMap.get(DcMotor.class, "FrontIntake");


        Pose2d beginPose = new Pose2d(new Vector2d(-53, 47), Math.toRadians(128));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        // --- Create the Trajectory Action ---
        Action path = drive.actionBuilder(beginPose)

                .stopAndAdd(activateFlyWheels())
                .strafeTo(new Vector2d(-40, 34))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(-50, 0), Math.toRadians(180))





                .strafeToLinearHeading(new Vector2d(-9, 28), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(-8,63))
                .strafeTo(new Vector2d(-8,40))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40, 34), Math.toRadians(128))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeToLinearHeading(new Vector2d(18, 25), Math.toRadians(92))
                .stopAndAdd(intakeStack())
                .strafeTo(new Vector2d(18,65))
                .strafeTo(new Vector2d(18,45))
                .stopAndAdd(activateFlyWheels())
                .strafeToLinearHeading(new Vector2d(-40,34), Math.toRadians(128))
                .stopAndAdd(shootFrontIntake())
                .waitSeconds(2)
                .stopAndAdd(stopAll())
                .strafeTo(new Vector2d(-55,30))
                .build();

        Actions.runBlocking(new SequentialAction(path));
    }

    public Action shootFrontIntake() {
        return packet -> {
            launcherWheel.setPower(-1.0);
            leftFlyWheel.setPower(-0.7);
            rightFlyWheel.setPower(0.7);
            frontIntake.setPower(-1.0);
            return false;
        };
    }

    public Action intakeStack() {
        return packet -> {
            frontIntake.setPower(-1.0);
            return false;
        };
    }

    public Action stopIntake() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            return false;
        };
    }
    public Action activateFlyWheels()
    {
        return packet -> {
            leftFlyWheel.setPower(-0.7);
            rightFlyWheel.setPower(0.7);
            return false;
        };
    }

    public Action stopAll() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            leftFlyWheel.setPower(0.0);
            rightFlyWheel.setPower(0.0);
            return false;
        };
    }
}
