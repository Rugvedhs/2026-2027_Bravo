package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * parkclose Autonomous OpMode:
 * 1. Move forward 1 tile.
 * 2. Strafe 1 tile right.
 */
@Autonomous(name="parkclose", group="Autonomous")
public class parkclose extends LinearOpMode {

    private Driver driver;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        telemetry.addData("Status", "Initialized - parkclose");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // 1. Forward 1 tile
            telemetry.addData("Step", "1. Forward 1 Tile");
            telemetry.update();
            driver.forward_tiles(1.0, 0.5);

            // 2. Strafe 1 tile right
            telemetry.addData("Step", "2. Strafe 1 Tile Right");
            telemetry.update();
            driver.strafe_tiles(1.0, 0.5);

            telemetry.addData("Step", "Autonomous Complete - Parked Close");
            telemetry.update();
        }
    }
}
