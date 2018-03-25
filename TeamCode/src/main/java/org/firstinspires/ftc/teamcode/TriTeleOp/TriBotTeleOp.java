package org.firstinspires.ftc.teamcode.TriTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TriBot Iterative V3.6", group="Iterative Opmode Tri")

public class TriBotTeleOp extends OpMode
{
    private DcMotor leftDrive, rightDrive, backDrive = null;
    private Servo armServo = null;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        backDrive = hardwareMap.get(DcMotor.class, "backDrive");
        armServo = hardwareMap.get(Servo.class, "armDrive");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        backDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        armServo.scaleRange(0.34, 1.0);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        backDrive.setPower(0.0);
        armServo.setPosition(armServo.MAX_POSITION);

        telemetry.addData("Status", "Init Complete | Awaiting Start");
    }

    @Override
    public void start() {}

    @Override
    public void loop()
    {
        double axial = Range.clip((gamepad1.left_stick_y/4), -1, 1);
        double lateral = Range.clip((-gamepad1.left_stick_x/4), -1, 1);
        double yaw = Range.clip((-gamepad1.right_stick_x/4), -1, 1);

        double back = yaw + lateral;
        double left = yaw - axial - lateral * 0.5;
        double right = yaw + axial - lateral * 0.5;

        double max = Math.max(Math.max(Math.abs(back), Math.abs(right)), Math.abs(left));
        if (max > 1.0)
        {
            back /= max;
            right /= max;
            left /= max;
        }

        armServo.setPosition(gamepad1.dpad_up ? armServo.MAX_POSITION : (gamepad1.dpad_down ? armServo.MIN_POSITION : armServo.getPosition()));

        try
        {
            if (gamepad2.b)
            {
                backDrive.setPower(0.0);
                leftDrive.setPower(0.0);
                rightDrive.setPower(0.0);
            }
            else
            {
                backDrive.setPower(back);
                leftDrive.setPower(left);
                rightDrive.setPower(right);
            }
        }
        catch (Exception ignored)
        {
            backDrive.setPower(back);
            leftDrive.setPower(left);
            rightDrive.setPower(right);
        }

        telemetry.addData("Status:", "Nom-Nom-Nominal");
        telemetry.addData("Axes:", "Axial(%.2f), Lateral(%.2f), Yaw(%.2f)", axial, lateral, yaw);
        telemetry.addData("Wheels:", "Left(%.2f), Right(%.2f), Back(%.2f)", left, right, back);
        telemetry.addData("Servo:", "Pos(%.2f)", armServo.getPosition());
    }

    @Override
    public void stop()
    {
        backDrive.setPower(0.0);
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        armServo.setPosition(armServo.MAX_POSITION);

        telemetry.addData("Status", "Stopped");
    }
}
