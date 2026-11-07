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
    public DcMotorEx MotorfL, MotorbL, MotorfR, MotorbR;

    // Modifiers set to 1 so setPowerFL/BL/FR/BR write exact physical directional powers directly
    protected final int MOTOR_fL_MODIFIER = 1;
    protected final int MOTOR_bL_MODIFIER = 1;
    protected final int MOTOR_fR_MODIFIER = 1;
    protected final int MOTOR_bR_MODIFIER = 1;

    protected boolean encoders_initialized = false;

    public static double TICKS_PER_REV = 28.0;

    public DriveTrain(HardwareMap map)
    {
        this(map, "frontLeft", "backLeft", "frontRight", "backRight");
    }

    public DriveTrain(HardwareMap map, String FL, String BL, String FR, String BR)
    {
        MotorfL = getMotorSafe(map, FL, "frontLeft", "frontleftdri", "leftFront", "fL", "FL");
        MotorbL = getMotorSafe(map, BL, "backLeft", "backleftdri", "leftbackdri", "leftBack", "bL", "BL");
        MotorfR = getMotorSafe(map, FR, "frontRight", "frontrightdri", "rightFront", "fR", "FR");
        MotorbR = getMotorSafe(map, BR, "backRight", "backrightdri", "rightbackdri", "rightBack", "bR", "BR");

        // GM0 Standard Motor Directions:
        // Left side motors REVERSE so positive power moves left wheels FORWARD physically
        if (MotorfL != null) MotorfL.setDirection(DcMotor.Direction.REVERSE);
        if (MotorbL != null) MotorbL.setDirection(DcMotor.Direction.REVERSE);

        // Right side motors FORWARD so positive power moves right wheels FORWARD physically
        if (MotorfR != null) MotorfR.setDirection(DcMotor.Direction.FORWARD);
        if (MotorbR != null) MotorbR.setDirection(DcMotor.Direction.FORWARD);

        // Default to open-loop for teleop responsiveness
        setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);

        // BRAKE behavior stops drifting instantly
        setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        Stop();
    }

    private DcMotorEx getMotorSafe(HardwareMap map, String primaryName, String... fallbacks) {
        if (primaryName != null) {
            try { return map.get(DcMotorEx.class, primaryName); } catch (Exception ignored) {}
        }
        for (String name : fallbacks) {
            try { return map.get(DcMotorEx.class, name); } catch (Exception ignored) {}
        }
        return null;
    }

    /**
     * TeleOp Driving Controls (GM0 Mecanum Matrix):
     * - Right Stick Y: Forward / Backward
     * - Right Stick X: Turn Left / Turn Right
     * - Left Stick X: Strafe Left / Strafe Right
     */
    public void Drive(Gamepad gamepad)
    {
        // 1. Right Stick Y = Forward / Backward
        double forward = -gamepad.right_stick_y;

        // 2. Right Stick X = Turn Left / Turn Right
        double turn = gamepad.right_stick_x;

        // 3. Left Stick X = Strafe Left / Strafe Right
        double strafe = gamepad.left_stick_x;

        // Deadzone check for stick drift
        final double DZ = 0.08;
        forward = Math.abs(forward) < DZ ? 0.0 : forward;
        turn    = Math.abs(turn)    < DZ ? 0.0 : turn;
        strafe  = Math.abs(strafe)  < DZ ? 0.0 : strafe;

        // Hard Stop when joysticks are released (eliminates persistent drifting/spinning!)
        if (forward == 0.0 && turn == 0.0 && strafe == 0.0) {
            Stop();
            return;
        }

        // Global speed modifier (Right Bumper = 100% Fast, B Button = 40% Slow)
        double speedMod = calculateSpeedModifier(gamepad, 0.4, 0.8, 1.0);

        // GM0 Standard Mecanum wheel power mix:
        // fl = forward + strafe + turn
        // fr = forward - strafe - turn
        // bl = forward - strafe + turn
        // br = forward + strafe - turn
        double fl = forward + strafe + turn;
        double fr = forward - strafe - turn;
        double bl = forward - strafe + turn;
        double br = forward + strafe - turn;

        // Normalize
        double max = Math.abs(fl);
        max = Math.max(max, Math.abs(fr));
        max = Math.max(max, Math.abs(bl));
        max = Math.max(max, Math.abs(br));
        if (max < 1.0) max = 1.0;
        fl /= max; fr /= max; bl /= max; br /= max;

        // Apply speed modifier
        fl *= speedMod; fr *= speedMod; bl *= speedMod; br *= speedMod;

        // Write motor power
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

    protected void setDirection(DcMotorEx.Direction direction)
    {
        if (MotorfL != null) MotorfL.setDirection(direction);
        if (MotorbL != null) MotorbL.setDirection(direction);
        if (MotorfR != null) MotorfR.setDirection(direction);
        if (MotorbR != null) MotorbR.setDirection(direction);
    }

    public void initEncoders()
    {
        setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
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
        if (MotorfL != null) MotorfL.setPower(power * MOTOR_fL_MODIFIER);
        if (MotorbL != null) MotorbL.setPower(power * MOTOR_bL_MODIFIER);
        if (MotorfR != null) MotorfR.setPower(power * MOTOR_fR_MODIFIER);
        if (MotorbR != null) MotorbR.setPower(power * MOTOR_bR_MODIFIER);
    }

    protected void setPowerFL(double power)
    {
        if (MotorfL != null) MotorfL.setPower(power * MOTOR_fL_MODIFIER);
    }

    protected void setPowerBL(double power)
    {
        if (MotorbL != null) MotorbL.setPower(power * MOTOR_bL_MODIFIER);
    }

    protected void setPowerFR(double power)
    {
        if (MotorfR != null) MotorfR.setPower(power * MOTOR_fR_MODIFIER);
    }

    protected void setPowerBR(double power)
    {
        if (MotorbR != null) MotorbR.setPower(power * MOTOR_bR_MODIFIER);
    }

    public void setMode(DcMotorEx.RunMode mode)
    {
        if (MotorfL != null) MotorfL.setMode(mode);
        if (MotorbL != null) MotorbL.setMode(mode);
        if (MotorfR != null) MotorfR.setMode(mode);
        if (MotorbR != null) MotorbR.setMode(mode);
    }

    public void setTargetPosition(int ticks)
    {
        if (MotorfL != null) MotorfL.setTargetPosition(ticks);
        if (MotorbL != null) MotorbL.setTargetPosition(ticks);
        if (MotorfR != null) MotorfR.setTargetPosition(ticks);
        if (MotorbR != null) MotorbR.setTargetPosition(ticks);
    }

    public boolean isBusy()
    {
        return (MotorfL != null && MotorfL.isBusy()) ||
               (MotorbL != null && MotorbL.isBusy()) ||
               (MotorfR != null && MotorfR.isBusy()) ||
               (MotorbR != null && MotorbR.isBusy());
    }

    public void setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior behavior)
    {
        if (MotorfL != null) MotorfL.setZeroPowerBehavior(behavior);
        if (MotorbL != null) MotorbL.setZeroPowerBehavior(behavior);
        if (MotorfR != null) MotorfR.setZeroPowerBehavior(behavior);
        if (MotorbR != null) MotorbR.setZeroPowerBehavior(behavior);
    }

    public int getMotorFLPosition() { return MotorfL != null ? MotorfL.getCurrentPosition() : 0; }
    public int getMotorFRPosition() { return MotorfR != null ? MotorfR.getCurrentPosition() : 0; }
    public int getMotorBLPosition() { return MotorbL != null ? MotorbL.getCurrentPosition() : 0; }
    public int getMotorBRPosition() { return MotorbR != null ? MotorbR.getCurrentPosition() : 0; }

    public double getMotorFLVel() { return MotorfL != null ? MotorfL.getVelocity() : 0.0; }
    public double getMotorFRVel() { return MotorfR != null ? MotorfR.getVelocity() : 0.0; }
    public double getMotorBLVel() { return MotorbL != null ? MotorbL.getVelocity() : 0.0; }
    public double getMotorBRVel() { return MotorbR != null ? MotorbR.getVelocity() : 0.0; }

    public double ticksPerSecToRPM(double ticksPerSec)
    {
        return (ticksPerSec / TICKS_PER_REV) * 60.0;
    }

    public void addDriveTelemetry(Telemetry telemetry)
    {
        telemetry.addLine("---- Wheel Speed (ticks/sec) ----");
        telemetry.addData("FL vel", "%.1f", getMotorFLVel());
        telemetry.addData("FR vel", "%.1f", getMotorFRVel());
        telemetry.addData("BL vel", "%.1f", getMotorBLVel());
        telemetry.addData("BR vel", "%.1f", getMotorBRVel());

        telemetry.addLine("---- Drive Encoders ----");
        telemetry.addData("FL pos", getMotorFLPosition());
        telemetry.addData("FR pos", getMotorFRPosition());
        telemetry.addData("BL pos", getMotorBLPosition());
        telemetry.addData("BR pos", getMotorBRPosition());
    }
}
