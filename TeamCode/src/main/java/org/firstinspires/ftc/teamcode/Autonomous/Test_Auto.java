package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Test_Auto", group="Autonomous")
public class Test_Auto extends LinearOpMode {

    Driver driver;
    public CRServo backBottom;
    public CRServo backIntake;
    public CRServo launcherWheel;
    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;
    public CRServo leftBelt;
    public CRServo rightBelt;
    public CRServo frontIntake;


    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);
        backBottom = (hardwareMap.get(CRServo.class, "BackBottom"));
        backIntake = (hardwareMap.get(CRServo.class, "BackIntake"));
        launcherWheel = (hardwareMap.get(CRServo.class, "LauncherWheel"));
        leftFlyWheel = (hardwareMap.get(DcMotor.class, "leftFly"));
        rightFlyWheel = (hardwareMap.get(DcMotor.class, "rightFly"));
        leftBelt = (hardwareMap.get(CRServo.class, "LeftBelt"));
        rightBelt = (hardwareMap.get(CRServo.class, "RightBelt"));
        frontIntake = (hardwareMap.get(CRServo.class, "FrontIntake"));

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();

        if (opModeIsActive()) {

        }

    }

}
