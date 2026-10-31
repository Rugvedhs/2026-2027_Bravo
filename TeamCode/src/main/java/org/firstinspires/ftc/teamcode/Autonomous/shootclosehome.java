package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="shootclosehome", group="Autonomous")
public class shootclosehome extends LinearOpMode
{
    Driver driver;
    public DcMotorEx leftLauncher;
    public DcMotor frontIntake;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        leftLauncher = getDeviceSafe(DcMotorEx.class, "leftlauncher", "leftLauncher");
        frontIntake  = getDeviceSafe(DcMotor.class, "frontintake", "FrontIntake");

        if (leftLauncher != null) {
            leftLauncher.setDirection(DcMotor.Direction.FORWARD);
            leftLauncher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            leftLauncher.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        if (frontIntake != null) {
            frontIntake.setDirection(DcMotorSimple.Direction.FORWARD);
        }

        telemetry.addData("Status", "Initialized - shootclosehome");
        telemetry.update();

        waitForStart();

        if (opModeIsActive())
        {
            // 1. Move forward 1 tile
            driver.forward_tiles(1.0, 0.5);

            // 2. Power ON left launcher flywheel and intake to shoot
            if (leftLauncher != null) leftLauncher.setPower(1.0);
            sleep(1000); // Spin up flywheel

            if (frontIntake != null) frontIntake.setPower(-1.0); // Pull balls in
            sleep(3500); // Shoot balls

            // 3. Stop mechanisms
            if (leftLauncher != null) leftLauncher.setPower(0.0);
            if (frontIntake != null) frontIntake.setPower(0.0);

            // 4. Strafe 1 tile right
            driver.strafe_tiles(1.0, 0.5);
        }
    }

    private <T> T getDeviceSafe(Class<T> classId, String primaryName, String... fallbacks) {
        try { return hardwareMap.get(classId, primaryName); } catch (Exception ignored) {}
        for (String name : fallbacks) {
            try { return hardwareMap.get(classId, name); } catch (Exception ignored) {}
        }
        return null;
    }
}
