package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="ParkFar", group="Autonomous")
public class ParkFar extends LinearOpMode {

    private Driver driver;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);

        telemetry.addData("Status", "Initialized - ParkFar");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Step", "Strafing to Far Park Zone");
            telemetry.update();

            driver.strafe_tiles(-1.0, 0.8); // Strafe Left 1 tile into Far Park Zone

            telemetry.addData("Status", "ParkFar Complete");
            telemetry.update();
        }
    }
}
