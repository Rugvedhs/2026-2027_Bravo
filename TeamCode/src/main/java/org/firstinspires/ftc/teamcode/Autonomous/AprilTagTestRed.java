package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Stuff.MecanumDrive;

import java.util.List;

@Autonomous(name = "RedAuto", group = "Autonomous")
public class AprilTagTestRed extends LinearOpMode {

    private Limelight3A limelight;

    public DcMotor launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public DcMotor frontIntake;

    // Use ONE AprilTag pipeline here
    // Change this if your real AprilTag pipeline is not 7
    private static final int APRILTAG_PIPELINE = 0;

    @Override
    public void runOpMode() throws InterruptedException {

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.setPollRateHz(100);
        limelight.start();

        launcherWheel = hardwareMap.get(DcMotor.class, "LauncherWheel");
        leftFlyWheel = hardwareMap.get(DcMotor.class, "leftFly");
        rightFlyWheel = hardwareMap.get(DcMotor.class, "rightFly");
        frontIntake = hardwareMap.get(DcMotor.class, "FrontIntake");

        Pose2d beginPose = new Pose2d(new Vector2d(-53, 47), Math.toRadians(128));
        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("AprilTag Pipeline", APRILTAG_PIPELINE);
        telemetry.update();

        waitForStart();

        if (isStopRequested()) return;

        if (opModeIsActive()) {
            Action launchSequence = drive.actionBuilder(beginPose)
                    .stopAndAdd(activateFlyWheels())
                    .strafeTo(new Vector2d(-40, 34))
                    .stopAndAdd(shootFrontIntake())
                    .waitSeconds(2)
                    .stopAndAdd(stopAll())
                    .strafeToLinearHeading(new Vector2d(-54, 0), Math.toRadians(180))
                    .build();

            Actions.runBlocking(new SequentialAction(launchSequence));

            Pose2d afterLaunchPose = drive.localizer.getPose();

            Action toScanPosition = drive.actionBuilder(afterLaunchPose)
                    .build();

            Actions.runBlocking(new SequentialAction(toScanPosition));

            int detectedTag = detectTag();

            telemetry.addData("Detected Tag", detectedTag);
            telemetry.update();
        }

        limelight.stop();
    }

    private int detectTag() {
        limelight.pipelineSwitch(APRILTAG_PIPELINE);

        long startTime = System.currentTimeMillis();

        while (opModeIsActive() && System.currentTimeMillis() - startTime < 2000) {
            LLResult result = limelight.getLatestResult();

            if (result == null) {
                telemetry.addLine("No Limelight result yet");
                telemetry.update();
                sleep(20);
                continue;
            }

            telemetry.addData("Requested Pipeline", APRILTAG_PIPELINE);
            telemetry.addData("Current Pipeline", result.getPipelineIndex());
            telemetry.addData("Valid", result.isValid());
            telemetry.addData("Target Area", result.getTa());
            telemetry.addData("Staleness ms", result.getStaleness());

            if (result.getPipelineIndex() != APRILTAG_PIPELINE) {
                telemetry.addLine("Waiting for pipeline switch...");
                telemetry.update();
                sleep(20);
                continue;
            }

            if (result.getStaleness() > 100) {
                telemetry.addLine("Result too stale, waiting...");
                telemetry.update();
                sleep(20);
                continue;
            }

            if (result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();

                if (fiducials != null && !fiducials.isEmpty()) {
                    for (LLResultTypes.FiducialResult fiducial : fiducials) {
                        int id = fiducial.getFiducialId();

                        telemetry.addData("Found Fiducial ID", id);
                        telemetry.update();

                        if (id == 21 || id == 22 || id == 23) {
                            return id;
                        }
                    }
                } else {
                    telemetry.addLine("Valid result, but no fiducials found");
                    telemetry.update();
                }
            } else {
                telemetry.addLine("No valid target");
                telemetry.update();
            }

            sleep(20);
        }

        telemetry.addLine("No tag detected, defaulting to 23");
        telemetry.update();
        return 23;
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

    public Action stopAll() {
        return packet -> {
            launcherWheel.setPower(0);
            frontIntake.setPower(0);
            leftFlyWheel.setPower(0);
            rightFlyWheel.setPower(0);
            return false;
        };
    }

    public Action activateFlyWheels() {
        return packet -> {
            leftFlyWheel.setPower(-0.7);
            rightFlyWheel.setPower(0.7);
            return false;
        };
    }

    public Action stopFlywheels() {
        return packet -> {
            rightFlyWheel.setPower(0);
            leftFlyWheel.setPower(0);
            return false;
        };
    }
}