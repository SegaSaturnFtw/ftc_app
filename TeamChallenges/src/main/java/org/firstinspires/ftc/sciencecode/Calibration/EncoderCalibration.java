package org.firstinspires.ftc.sciencecode.Calibration;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name="Encoder Calib V1.0", group="Calibration")
public class EncoderCalibration extends OpMode
{
    private final double LEFT_RATE = 40.0;
    private final double RIGHT_RATE = 40.0;
    private final double TARGET = 100.0;

    private DcMotor leftDrive;
    private DcMotor rightDrive;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);
    }

    @Override
    public void start()
    {
        leftDrive.setTargetPosition((int) (TARGET * LEFT_RATE));
        rightDrive.setTargetPosition((int) (TARGET * RIGHT_RATE));

        leftDrive.setPower(25);
        rightDrive.setPower(25);
    }

    @Override
    public void loop()
    {
        if (leftDrive.getCurrentPosition() > leftDrive.getTargetPosition())
        {
            leftDrive.setPower(0.0);
        }

        if (rightDrive.getCurrentPosition() > rightDrive.getTargetPosition())
        {
            rightDrive.setPower(0.0);
        }
    }
}
