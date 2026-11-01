package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name="ParkFar", group="Autonomous")
public class ParkFar extends LinearOpMode
{
    Driver driver;
    public DcMotorEx leftShooterRoller;
    public DcMotorEx rightShooterRoller;
    public DcMotorEx frontIntake;
    public CRServo leftPusher;
    public CRServo rightPusher;
    public CRServo splitter;


    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);
        leftShooterRoller   = (hardwareMap.get(DcMotorEx.class, "BackBottom"));
        rightShooterRoller   = (hardwareMap.get(DcMotorEx.class, "BackIntake"));
        leftPusher = (hardwareMap.get(CRServo.class, "LeftBelt"));
        rightPusher = (hardwareMap.get(CRServo.class, "RightBelt"));
        frontIntake = (hardwareMap.get(DcMotorEx.class, "FrontIntake"));
        splitter = (hardwareMap.get(CRServo.class, "FrontIntake"));

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        if (opModeIsActive())
        {
            // this section puts the robot in position to shoot the 2 artifacts in the back intake
            driver.forward_tiles(-0.4);
            sleep(3000);
//-------------------------------------------------------------------------------
            // this section turns on all the things needed to shoot the artifact in front intake
            sleep(6000);
//--------------------------------------------------------------------------------------------------
            // this section turns off all the parts to ensure nothing else happens that might give us penalty

//--------------------------------------------------------------------------------------------------
            // this section moves the robot out of the launch zone to get move points
            driver.turn_ticks(390,1);
            driver.forward_tiles(0.4,1.0);
            driver.strafe_tiles(-0.8,1.0);
        }

    }
}