package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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

    private long yPressedTime = 0;

    @Override
    public void runOpMode() {
        driveTrain = new DriveTrain(
                hardwareMap,
                "leftFront", "leftBack",
                "rightFront", "rightBack"
        );
        launcherWheel = new LauncherWheel(
                hardwareMap.get(DcMotor.class, "LauncherWheel")
        );
        frontIntake = new FrontIntake(
                hardwareMap.get(DcMotor.class, "FrontIntake")
        );
        flyWheels = new FlyWheels(
                hardwareMap.get(DcMotorEx.class, "leftFly"),
                hardwareMap.get(DcMotorEx.class, "rightFly")
        );

        launcherWheel.init();
        flyWheels.init();
        frontIntake.init();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            driveTrain.Drive(gamepad1);

            boolean overrideAll = gamepad2.y;
            boolean shootPressed = gamepad2.a;

            if (overrideAll && yPressedTime == 0) {
                yPressedTime = System.currentTimeMillis();
            } else if (!overrideAll) {
                yPressedTime = 0;
            }

            boolean launcherAndIntakeReady = overrideAll &&
                    yPressedTime != 0 &&
                    System.currentTimeMillis() - yPressedTime >= 500;
            if (shootPressed) {
                launcherWheel.stop();
            } else if (launcherAndIntakeReady) {
                launcherWheel.setPower(-1.0);
            } else {
                launcherWheel.setPower(-0.10);
            }
            if (overrideAll) {
                frontIntake.update(1f, false);
            } else {
                frontIntake.update(gamepad2.right_stick_y, gamepad2.a);
            }
            flyWheels.update(
                    gamepad2.right_bumper,
                    gamepad2.left_bumper,
                    gamepad2.x,
                    overrideAll
            );

            telemetry.update();
            flyWheels.getVelocityAndError(telemetry);
        }
    }
}
