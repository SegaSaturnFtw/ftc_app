package org.firstinspires.ftc.sciencecode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Default Claw Tele Op V3.0", group="Iterative OpMode Default")
public class ClawTeleOpMain extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor leftDrive, rightDrive, armDrive = null;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        telemetry.addData("Status:", "Initialized");
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status:", "Awaiting Start | Vehicle");
    }

    @Override
    public void start()
    {
        runtime.reset();

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop()
    {
        try
        {
            leftDrive.setPower(gamepad2.b ? 0.0 : ((Math.pow(gamepad1.left_stick_y, 2) * (gamepad1.left_stick_y / 2)) * 1.6));
            rightDrive.setPower(gamepad2.b ? 0.0 : ((Math.pow(gamepad1.right_stick_y, 2) * (gamepad1.right_stick_y / 2)) * 1.6));
        }
        catch (Exception ignored)
        {
            leftDrive.setPower((Math.pow(gamepad1.left_stick_y, 2) * (gamepad1.left_stick_y / 2)) * 1.6);
            rightDrive.setPower((Math.pow(gamepad1.right_stick_y, 2) * (gamepad1.right_stick_y / 2)) * 1.6);
        }

        telemetryStandard();
    }

    void telemetryStandard()
    {
        telemetry.addData("Status:", "Running Nominally: " + runtime.toString());
        telemetry.addData("Motors:", "Left (%.2f), Right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
        telemetry.addData("Joysticks P1:", "Left (%.2f), Right (%.2f)", gamepad1.left_stick_y, gamepad1.right_stick_y);
    }

    @Override
    public void stop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        telemetry.addData("Status:", "Stopped");
    }
}