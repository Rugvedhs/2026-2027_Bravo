package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * parkfar Autonomous OpMode:
 * 1. Move forward 1 tile.
 * 2. Strafe 1 tile left.
 * 3. Move forward 4 tiles.
 * 4. Strafe 1 tile left.
 */
@Autonomous(name="parkfar", group="Autonomous")
public class parkfar extends LinearOpMode {

    private Driver driver;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        telemetry.addData("Status", "Initialized - parkfar");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // 1. Forward 1 tile
            telemetry.addData("Step", "1. Forward 1 Tile");
            telemetry.update();
            driver.forward_tiles(1.0, 0.5);

            // 2. Strafe 1 tile left
            telemetry.addData("Step", "2. Strafe 1 Tile Left");
            telemetry.update();
            driver.strafe_tiles(-1.0, 0.5);

            // 3. Forward 4 tiles
            telemetry.addData("Step", "3. Forward 4 Tiles");
            telemetry.update();
            driver.forward_tiles(4.0, 0.6);

            // 4. Strafe 1 tile left
            telemetry.addData("Step", "4. Strafe 1 Tile Left");
            telemetry.update();
            driver.strafe_tiles(-1.0, 0.5);

            telemetry.addData("Step", "Autonomous Complete - Parked Far");
            telemetry.update();
        }
    }
}
