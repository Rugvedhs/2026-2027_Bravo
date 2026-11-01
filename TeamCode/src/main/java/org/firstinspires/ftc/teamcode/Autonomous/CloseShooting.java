package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="CloseShooting", group="Autonomous")
public class CloseShooting extends LinearOpMode
{
    Driver driver;
    public DcMotorEx LeftShooterRoller;
    public DcMotorEx RightShooterRoller;
    public DcMotorEx FrontIntake;
    public CRServo LeftPusher;
    public CRServo RightPusher;
    public CRServo Splitter;




    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);
        LeftShooterRoller = (hardwareMap.get(DcMotorEx.class, "leftFly"));
        RightShooterRoller = (hardwareMap.get(DcMotorEx.class, "rightFly"));
        FrontIntake = (hardwareMap.get(DcMotorEx.class, "FrontIntake"));

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        if (opModeIsActive())
        {
            // this section puts the robot in position to shoot the 2 artifacts in the back intake
            driver.forward_tiles(-0.4);
            backIntake.setPower(-1.0);
            backBottom.setPower(-1.0);
            launcherWheel.setPower(1.0);
            leftFlyWheel.setPower(-0.85);
            rightFlyWheel.setPower(0.85);
            sleep(3000);
//-------------------------------------------------------------------------------
            // this section turns on all the things needed to shoot the artifact in front intake
            backBottom.setPower(1.0);
            sleep(6000);
//--------------------------------------------------------------------------------------------------
            // this section turns off all the parts to ensure nothing else happens that might give us penalty
            backBottom.setPower(0);
            launcherWheel.setPower(0);
            backIntake.setPower(0);
            leftFlyWheel.setPower(0.0);
            rightFlyWheel.setPower(0.0);
//--------------------------------------------------------------------------------------------------
            // this section moves the robot out of the launch zone to get move points
            driver.turn_ticks(-390,1);
            driver.forward_tiles(0.4,1.0);
            driver.strafe_tiles(0.8,1.0);
        }

    }
}