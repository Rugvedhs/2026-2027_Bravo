package org.firstinspires.ftc.teamcode.Core;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;


/**
 * Basic drivetrain helper. Supports direct open-loop driving (teleop)
 * and helper methods for encoder-based control (autonomous).
 */
public class DriveTrain
{
    public DcMotorEx MotorfL, MotorbL;
    public DcMotorEx MotorfR, MotorbR;

    // Direction modifiers (1 or -1) in case a motor needs reversing for power calls
    // <-- LEFT MOTORS inverted to correct physical wiring (front & back left were spinning opposite)
    protected final int MOTOR_fL_MODIFIER = -1;
    protected final int MOTOR_bL_MODIFIER = -1;
    protected final int MOTOR_fR_MODIFIER = 1;
    protected final int MOTOR_bR_MODIFIER = 1;

    protected boolean encoders_initialized = false;

    // If you want RPM, set this correctly for YOUR motor encoder
    // Common:
    // - goBILDA 5202/5203 435RPM = 28 ticks/rev (on motor encoder)
    // - goBILDA 312RPM = 28 ticks/rev
    // - REV HD Hex motor = 28 ticks/rev (built-in encoder)
    // If you're unsure, you can still compare wheel speeds using ticks/sec without RPM.
    public static double TICKS_PER_REV = 28.0;

    public DriveTrain(HardwareMap map, String FL, String BL, String FR, String BR)
    {
        MotorfL = map.get(DcMotorEx.class, FL);
        MotorbL = map.get(DcMotorEx.class, BL);
        MotorfR = map.get(DcMotorEx.class, FR);
        MotorbR = map.get(DcMotorEx.class, BR);

        // Default direction: if your physical wiring makes a wheel spin wrong way, reverse here.
        setDirection(DcMotor.Direction.FORWARD);

        // Default to open-loop for teleop responsiveness
        setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Default zero power behavior: BRAKE makes autonomous moves stop more reliably.
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        Stop();
    }

    /**
     * Teleop driving: mix turn/forward/strafe and write motor power once (prevents overwriting).
     */
    public void Drive(Gamepad gamepad)
    {
        // joystick axes: push up = negative on most controllers, so invert Y
        double forward = -gamepad.left_stick_y;   // forward positive
        double turn    =  gamepad.left_stick_x;   // right positive
        double strafe  =  gamepad.right_stick_x;  // right positive

        // deadzone
        final double DZ = 0.3; // 0.08
        forward = Math.abs(forward) < DZ ? 0.0 : forward;
        turn    = Math.abs(turn)    < DZ ? 0.0 : turn;
        strafe  = Math.abs(strafe)  < DZ ? 0.0 : strafe;

        // global speed modifier (use right bumper for fast, 'b' for slow)
        double speedMod = calculateSpeedModifier(gamepad, 0.6, 0.8, 1.0);

        // mecanum mix
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        // normalize
        double max = Math.abs(fl);
        max = Math.max(max, Math.abs(fr));
        max = Math.max(max, Math.abs(bl));
        max = Math.max(max, Math.abs(br));
        if (max < 1.0) max = 1.0;
        fl /= max; fr /= max; bl /= max; br /= max;

        // apply speed
        fl *= speedMod; fr *= speedMod; bl *= speedMod; br *= speedMod;

        // write motor power
        setPowerFL(fl);
        setPowerFR(fr);
        setPowerBL(bl);
        setPowerBR(br);
    }

    public void Stop()
    {
        setPower(0);
    }

    protected void Forward(Gamepad gamepad, float speed)
    {
        double speedMod = calculateSpeedModifier(gamepad, 0.3, 0.8, 1.0);
        DirectForward(speed * speedMod);
    }

    protected void Strafe(Gamepad gamepad, float speed)
    {
        double speedMod = calculateSpeedModifier(gamepad, 0.3, 0.8, 1.0);
        DirectStrafe(speed * speedMod);
    }

    public void Turn(Gamepad gamepad, float speed)
    {
        double speedMod = calculateSpeedModifier(gamepad, 0.3, 0.8, 1.0);
        DirectTurn(speed * speedMod);
    }

    protected double calculateSpeedModifier(@NonNull Gamepad gamepad, double slow, double normal, double fast)
    {
        if (gamepad.right_bumper) return fast;
        if (gamepad.b) return slow;
        return normal;
    }

    protected void setDirection(DcMotor.Direction direction)
    {
        MotorfL.setDirection(direction);
        MotorbL.setDirection(direction);
        MotorfR.setDirection(direction);
        MotorbR.setDirection(direction);
    }

    /**
     * Initialize encoders: reset and switch to RUN_USING_ENCODER.
     */
    public void initEncoders()
    {
        setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        encoders_initialized = true;
    }

    public void DirectForward(double speed)
    {
        setPowerFR(speed);
        setPowerFL(speed);
        setPowerBL(speed);
        setPowerBR(speed);
    }

    public void DirectTurn(double speed)
    {
        setPowerFR(-speed);
        setPowerFL(speed);
        setPowerBL(speed);
        setPowerBR(-speed);
    }

    protected void DirectStrafe(double speed)
    {
        setPowerFR(-speed);
        setPowerFL(speed);
        setPowerBL(-speed);
        setPowerBR(speed);
    }

    public void setPower(double power)
    {
        MotorfL.setPower(power * MOTOR_fL_MODIFIER);
        MotorbL.setPower(power * MOTOR_bL_MODIFIER);
        MotorfR.setPower(power * MOTOR_fR_MODIFIER);
        MotorbR.setPower(power * MOTOR_bR_MODIFIER);
    }

    protected void setPowerFL(double power)
    {
        MotorfL.setPower(power * MOTOR_fL_MODIFIER);
    }

    protected void setPowerBL(double power)
    {
        MotorbL.setPower(power * MOTOR_bL_MODIFIER);
    }

    protected void setPowerFR(double power)
    {
        MotorfR.setPower(power * MOTOR_fR_MODIFIER);
    }

    protected void setPowerBR(double power)
    {
        MotorbR.setPower(power * MOTOR_bR_MODIFIER);
    }

    public void setMode(DcMotor.RunMode mode)
    {
        MotorfL.setMode(mode);
        MotorbL.setMode(mode);
        MotorfR.setMode(mode);
        MotorbR.setMode(mode);
    }

    public void setTargetPosition(int ticks)
    {
        MotorfL.setTargetPosition(ticks);
        MotorbL.setTargetPosition(ticks);
        MotorfR.setTargetPosition(ticks);
        MotorbR.setTargetPosition(ticks);
    }

    public boolean isBusy()
    {
        return MotorfL.isBusy() || MotorbL.isBusy() || MotorfR.isBusy() || MotorbR.isBusy();
    }

    public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior)
    {
        MotorfL.setZeroPowerBehavior(behavior);
        MotorbL.setZeroPowerBehavior(behavior);
        MotorfR.setZeroPowerBehavior(behavior);
        MotorbR.setZeroPowerBehavior(behavior);
    }

    // Positions
    public int getMotorFLPosition() { return MotorfL.getCurrentPosition(); }
    public int getMotorFRPosition() { return MotorfR.getCurrentPosition(); }
    public int getMotorBLPosition() { return MotorbL.getCurrentPosition(); }
    public int getMotorBRPosition() { return MotorbR.getCurrentPosition(); }

    // Velocities (ticks/sec)
    public double getMotorFLVel() { return MotorfL.getVelocity(); }
    public double getMotorFRVel() { return MotorfR.getVelocity(); }
    public double getMotorBLVel() { return MotorbL.getVelocity(); }
    public double getMotorBRVel() { return MotorbR.getVelocity(); }

    // RPM estimate
    public double ticksPerSecToRPM(double ticksPerSec)
    {
        return (ticksPerSec / TICKS_PER_REV) * 60.0;
    }

    /**
     * Call this from TeleOp loop to see encoder positions + speeds for all 4 wheels.
     */
    public void addDriveTelemetry(Telemetry telemetry)
    {

        telemetry.addLine("---- Wheel Speed (ticks/sec) ----");
        telemetry.addData("FL vel", "%.1f", getMotorFLVel());
        telemetry.addData("FR vel", "%.1f", getMotorFRVel());
        telemetry.addData("BL vel", "%.1f", getMotorBLVel());
        telemetry.addData("BR vel", "%.1f", getMotorBRVel());

        telemetry.addLine("---- Wheel Speed (RPM est) ----");
        telemetry.addData("FL rpm", "%.1f", ticksPerSecToRPM(getMotorFLVel()));
        telemetry.addData("FR rpm", "%.1f", ticksPerSecToRPM(getMotorFRVel()));
        telemetry.addData("BL rpm", "%.1f", ticksPerSecToRPM(getMotorBLVel()));
        telemetry.addData("BR rpm", "%.1f", ticksPerSecToRPM(getMotorBRVel()));

        telemetry.addLine("---- Drive Encoders ----");
        telemetry.addData("FL pos", getMotorFLPosition());
        telemetry.addData("FR pos", getMotorFRPosition());
        telemetry.addData("BL pos", getMotorBLPosition());
        telemetry.addData("BR pos", getMotorBRPosition());

    }

}