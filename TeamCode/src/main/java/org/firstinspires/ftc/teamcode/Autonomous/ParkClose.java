package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="ParkClose", group="Autonomous")
public class ParkClose extends LinearOpMode {

    private Driver driver;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        telemetry.addData("Status", "Initialized - ParkClose");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Step", "Driving to Close Park Zone");
            telemetry.update();

            driver.forward_tiles(1.0, 0.8); // Drive Forward 1 tile into Close Park Zone

            telemetry.addData("Status", "ParkClose Complete");
            telemetry.update();
        }
    }
}
