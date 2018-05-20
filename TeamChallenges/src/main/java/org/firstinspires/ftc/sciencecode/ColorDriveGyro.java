package org.firstinspires.ftc.sciencecode;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Gyro Color Challenge V1.0", group="OpMode")
public class ColorDriveGyro extends OpMode
{
    private DcMotor leftDrive;
    private DcMotor rightDrive;

    private ModernRoboticsI2cGyro gyroSensor;
    private ColorSensor colorSensor;

    private static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    private static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    private static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    private static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    // These constants define the desired driving/control characteristics
    // The can/should be tweaked to suite the specific robot drive train.
    private static final double DRIVE_SPEED = 0.7;     // Nominal speed for better accuracy.
    private static final double TURN_SPEED = 0.5;     // Nominal half speed for better accuracy.

    private static final double HEADING_THRESHOLD = 1;      // As tight as we can make it with an integer gyro
    private static final double P_TURN_COEFF  = 0.1;     // Larger is more responsive, but also less stable
    private static final double P_DRIVE_COEFF = 0.15;     // Larger is more responsive, but also less stable

    @Override
    public void init()
    {
        gyroSensor = hardwareMap.get(ModernRoboticsI2cGyro.class, "gyroSensor");
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop()
    {
        gyroDrive(TURN_SPEED, 10, 45);

    }

    private void gyroDrive (double speed, double distance, double angle)
    {
        int newLeftTarget;
        int newRightTarget;
        int moveCounts;
        double max;
        double error;
        double steer;
        double leftSpeed;
        double rightSpeed;

        boolean gyroDriveActive = true;

        // Ensure that the opmode is still active
        if (gyroDriveActive)
        {
            // Determine new target position, and pass to motor controller
            moveCounts = (int)(distance * COUNTS_PER_INCH);
            newLeftTarget = this.leftDrive.getCurrentPosition() + moveCounts;
            newRightTarget = this.rightDrive.getCurrentPosition() + moveCounts;

            // Set Target and Turn On RUN_TO_POSITION
            this.leftDrive.setTargetPosition(newLeftTarget);
            this.rightDrive.setTargetPosition(newRightTarget);

            this.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            this.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // start motion.
            speed = Range.clip(Math.abs(speed), 0.0, 1.0);
            this.leftDrive.setPower(speed);
            this.rightDrive.setPower(speed);

            // keep looping while we are still active, and BOTH motors are running.
            while (this.leftDrive.isBusy() && this.rightDrive.isBusy())
            {
                error = getError(angle);
                steer = getSteer(error, P_DRIVE_COEFF);

                // if driving in reverse, the motor correction also needs to be reversed
                if (distance < 0)
                    steer *= -1.0;

                leftSpeed = speed - steer;
                rightSpeed = speed + steer;

                // Normalize speeds if either one exceeds +/- 1.0;
                max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
                if (max > 1.0)
                {
                    leftSpeed /= max;
                    rightSpeed /= max;
                }

                this.leftDrive.setPower(leftSpeed);
                this.rightDrive.setPower(rightSpeed);

                // Display drive status for the driver.
                telemetry.addData("Err/St",  "%5.1f/%5.1f", error, steer);
                telemetry.addData("Target",  "%7d:%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Actual",  "%7d:%7d", this.leftDrive.getCurrentPosition(), this.rightDrive.getCurrentPosition());
                telemetry.addData("Speed",   "%5.2f:%5.2f", leftSpeed, rightSpeed);
                telemetry.update();
            }

            // Stop all motion;
            this.leftDrive.setPower(0);
            this.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            this.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            this.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            gyroDriveActive = false;
        }
    }

    public void gyroTurn (double speed, double angle)
    {
        // keep looping while we are still active, and not on heading.
        while (!onHeading(speed, angle, P_TURN_COEFF))
        {
            telemetry.update();
        }
    }

    public void gyroHold( double speed, double angle, double holdTime)
    {
        ElapsedTime holdTimer = new ElapsedTime();

        // keep looping while we have time remaining.
        holdTimer.reset();
        while (holdTimer.time() < holdTime)
        {
            // Update telemetry & Allow time for other processes to run.
            onHeading(speed, angle, P_TURN_COEFF);
            telemetry.update();
        }

        // Stop all motion;
        this.leftDrive.setPower(0);
        this.rightDrive.setPower(0);
    }

    private boolean onHeading(double speed, double angle, double PCoeff)
    {
        double   error ;
        double   steer ;
        boolean  onTarget = false ;
        double leftSpeed;
        double rightSpeed;

        error = getError(angle);

        if (Math.abs(error) <= HEADING_THRESHOLD)
        {
            steer = 0.0;
            leftSpeed  = 0.0;
            rightSpeed = 0.0;
            onTarget = true;
        }
        else
        {
            steer = getSteer(error, PCoeff);
            rightSpeed  = speed * steer;
            leftSpeed   = -rightSpeed;
        }
        this.leftDrive.setPower(leftSpeed);
        this.rightDrive.setPower(rightSpeed);

        telemetry.addData("Target", "%5.2f", angle);
        telemetry.addData("Err/St", "%5.2f/%5.2f", error, steer);
        telemetry.addData("Speed.", "%5.2f:%5.2f", leftSpeed, rightSpeed);

        return onTarget;
    }

    private double getError(double targetAngle)
    {
        double robotError;

        robotError = targetAngle - this.gyroSensor.getIntegratedZValue();
        while (robotError > 180)  robotError -= 360;
        while (robotError <= -180) robotError += 360;
        return robotError;
    }

    private double getSteer(double error, double PCoeff)
    {
        return Range.clip(error * PCoeff, -1, 1);
    }
}
