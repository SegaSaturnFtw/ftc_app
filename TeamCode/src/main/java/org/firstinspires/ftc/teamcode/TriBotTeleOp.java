package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TriBot Iterative V3.0", group="Iterative Opmode")

public class TriBotTeleOp extends OpMode
{
    private DcMotor leftDrive, rightDrive, backDrive = null;
    private Servo armServo = null;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "back drive");
        rightDrive = hardwareMap.get(DcMotor.class, "left drive");
        backDrive = hardwareMap.get(DcMotor.class, "right drive");
        armServo = hardwareMap.get(Servo.class, "claw arm");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        backDrive.setDirection(DcMotor.Direction.REVERSE);

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
        double yaw = Range.clip((gamepad1.right_stick_x/4), -1, 1);

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

        backDrive.setPower(back/2);
        leftDrive.setPower(left/2);
        rightDrive.setPower(right/2);

        armServo.setPosition(gamepad1.dpad_up ? armServo.MAX_POSITION : (gamepad1.dpad_down ? armServo.MIN_POSITION : armServo.getPosition()));

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
        armServo.setPosition(armServo.getPosition());

        telemetry.addData("Status", "Stopped");
    }
}
