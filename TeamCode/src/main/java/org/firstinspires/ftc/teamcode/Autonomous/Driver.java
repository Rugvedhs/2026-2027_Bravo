package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Core.DriveTrain;

/**
 * Driver extends DriveTrain with encoder-based high-level movement routines.
 */
public class Driver extends DriveTrain
{
    private final LinearOpMode opMode;

    /** The length of a tile in inches */
    int tileLength = 24;

    /** Ticks per rotation of the motor */
    private final double encoderResolution = 537.7;

    /** Wheel diameter in millimeters */
    private final double wheel_diameter = 96; // mm

    private final int ticks_per_90_turn = 935;

    /** linear distance covered by one rotation (mm) */
    private final double distance_per_motor_rotation = wheel_diameter * Math.PI; // mm per motor rotation

    /** Default timeout for any move (ms) */
    private static final long DEFAULT_MOVE_TIMEOUT_MS = 5000;

    public Driver(LinearOpMode opMode, HardwareMap map)
    {
        super(map, "frontleftdri", "backleftdri", "frontrightdri", "backrightdri");
        this.opMode = opMode;

        // Initialize encoders and ensure brake behavior for precise autonomous moves
        super.initEncoders();
        super.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Waits for RUN_TO_POSITION to finish, but is opMode-aware and has a timeout.
     */
    private void waitForMotionFinish(long timeoutMs)
    {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while (opMode.opModeIsActive() && super.isBusy() && timer.milliseconds() < timeoutMs)
        {
            opMode.telemetry.addData("FL pos", getMotorFLPosition());
            opMode.telemetry.addData("FR pos", getMotorFRPosition());
            opMode.telemetry.addData("BL pos", getMotorBLPosition());
            opMode.telemetry.addData("BR pos", getMotorBRPosition());
            opMode.telemetry.update();

            opMode.idle();
        }

        super.Stop();
        super.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    private void wait_to_finish()
    {
        waitForMotionFinish(DEFAULT_MOVE_TIMEOUT_MS);
    }

    private void forward_ticks(int ticks, double power)
    {
        int FL_target = super.getMotorFLPosition() + ticks;
        int BL_target = super.getMotorBLPosition() + ticks;
        int FR_target = super.getMotorFRPosition() + ticks;
        int BR_target = super.getMotorBRPosition() + ticks;

        if (super.MotorfL != null) super.MotorfL.setTargetPosition(FL_target);
        if (super.MotorbL != null) super.MotorbL.setTargetPosition(BL_target);
        if (super.MotorfR != null) super.MotorfR.setTargetPosition(FR_target);
        if (super.MotorbR != null) super.MotorbR.setTargetPosition(BR_target);

        super.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        super.setPower(power);

        wait_to_finish();
    }

    public void forward_distance(double distanceMm, double power)
    {
        int ticks = (int) (distanceMm * encoderResolution / distance_per_motor_rotation);
        forward_ticks(ticks, power);
    }

    public void forward_tiles(double tiles, double power)
    {
        double distanceMm = tiles * tileLength * 25.4;
        forward_distance(distanceMm, power);
    }

    public void forward_tiles(double tiles)
    {
        forward_tiles(tiles, 0.5);
    }

    private void strafe_ticks(int ticks, double power)
    {
        int FL_target = super.getMotorFLPosition() + ticks;
        int BL_target = super.getMotorBLPosition() - ticks;
        int FR_target = super.getMotorFRPosition() - ticks;
        int BR_target = super.getMotorBRPosition() + ticks;

        if (super.MotorfL != null) super.MotorfL.setTargetPosition(FL_target);
        if (super.MotorbL != null) super.MotorbL.setTargetPosition(BL_target);
        if (super.MotorfR != null) super.MotorfR.setTargetPosition(FR_target);
        if (super.MotorbR != null) super.MotorbR.setTargetPosition(BR_target);

        super.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        super.setPower(power);

        wait_to_finish();
    }

    public void strafe_distance(double distanceMm, double power)
    {
        int ticks = (int) (distanceMm * encoderResolution / distance_per_motor_rotation);
        strafe_ticks(ticks, power);
    }

    public void strafe_tiles(double tiles, double power)
    {
        strafe_distance(tiles * tileLength * 25.4, power);
    }

    public void strafe_tiles(double tiles)
    {
        strafe_tiles(tiles, 0.3);
    }

    public void turn_ticks(int ticks, double power)
    {
        int FL_target = super.getMotorFLPosition() + ticks;
        int BL_target = super.getMotorBLPosition() + ticks;
        int FR_target = super.getMotorFRPosition() - ticks;
        int BR_target = super.getMotorBRPosition() - ticks;

        if (super.MotorfL != null) super.MotorfL.setTargetPosition(FL_target);
        if (super.MotorbL != null) super.MotorbL.setTargetPosition(BL_target);
        if (super.MotorfR != null) super.MotorfR.setTargetPosition(FR_target);
        if (super.MotorbR != null) super.MotorbR.setTargetPosition(BR_target);

        super.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        super.setPower(power);

        wait_to_finish();
    }

    public void turn_90_intervals(int times)
    {
        turn_ticks(ticks_per_90_turn * times, 0.5);
    }

    public void turn_90_clockwise(int times)
    {
        turn_90_intervals(times);
    }

    public void turn_90_counter_clockwise(int times)
    {
        turn_90_intervals(-times);
    }
}
