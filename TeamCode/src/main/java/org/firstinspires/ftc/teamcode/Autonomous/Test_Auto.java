package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Test_Auto", group="Autonomous")
public class Test_Auto extends LinearOpMode {

    Driver driver;
    public DcMotor leftLauncher;
    public DcMotor rightLauncher;
    public CRServo beltServo;
    public CRServo pushServo;
    public CRServo splitterServo;

    @Override
    public void runOpMode() {
        driver = new Driver(this, hardwareMap);
        try { leftLauncher = hardwareMap.get(DcMotor.class, "leftLauncher"); } catch (Exception ignored) { leftLauncher = null; }
        try { rightLauncher = hardwareMap.get(DcMotor.class, "rightLauncher"); } catch (Exception ignored) { rightLauncher = null; }
        try { beltServo = hardwareMap.get(CRServo.class, "beltServo"); } catch (Exception ignored) { beltServo = null; }
        try { pushServo = hardwareMap.get(CRServo.class, "rightpush"); } catch (Exception ignored) { pushServo = null; }
        try { splitterServo = hardwareMap.get(CRServo.class, "splitterservo"); } catch (Exception ignored) { splitterServo = null; }

        telemetry.addData("Status", "Initialized - Test_Auto (Safe Mode)");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addData("Status", "Running Autonomous Test Sequence");
            telemetry.update();
            sleep(1000);
        }
    }
}
