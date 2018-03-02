package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TriBot Iterative V1.0 (TEST DEV)", group="Iterative Opmode")

public class TriBotTeleOp extends OpMode
{
    private DcMotor leftDrive, rightDrive, backDrive = null;
    private Servo armServo = null;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "left drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right drive");
        backDrive = hardwareMap.get(DcMotor.class, "back drive");
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

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        backDrive.setPower(0.0);
        armServo.setPosition(armServo.getPosition());

        telemetry.addData("Status", "Init Complete | Awaiting Start");
    }

    @Override
    public void start() {}

    @Override
    public void loop()
    {
        double brakes = Range.clip(Math.abs(gamepad1.right_trigger), 0, 1);
        double axial = Range.clip((gamepad1.left_stick_y/4), -1, 1);
        double lateral = (Range.clip((gamepad1.left_stick_x/4), -1, 1)) * 0.5;
        double yaw = Range.clip((gamepad1.right_stick_x/4), -1, 1);

        // the previous max code was removed for a range clip (seems to do the same thing UNTESTED) and a log function was added (remove if testing returns odd results)
        double back = Range.clip(Math.log(yaw + lateral), -1, 1);
        double left = Range.clip(Math.log(yaw - axial - lateral), -1, 1);
        double right = Range.clip(Math.log(yaw + axial - lateral), -1, 1);

        //used range clip to create a brake using the right trigger (subtracts from speed without overflowing or causing robot to move at 0 (TEST)
        backDrive.setPower(back > 0 ? Range.clip((back - brakes), 0, 1) : (back < 0 ? Range.clip((back + brakes), -1, 0) : 0.0));
        leftDrive.setPower(left > 0 ? Range.clip((left - brakes), 0, 1) : (left < 0 ? Range.clip((left + brakes), -1, 0) : 0.0));
        rightDrive.setPower(right > 0 ? Range.clip((right - brakes), 0, 1) : (right < 0 ? Range.clip((right + brakes), -1, 0) : 0.0));

        //removed try-catch in favor of range clipping (less error proof but cleaner? (test))
        armServo.setPosition(gamepad1.left_bumper ? Range.clip((armServo.getPosition() - 0.1), armServo.MIN_POSITION, armServo.MAX_POSITION) :
                (gamepad1.right_bumper ? Range.clip((armServo.getPosition() + 0.1), armServo.MIN_POSITION, armServo.MAX_POSITION) : armServo.getPosition()));

        telemetry.addData("Status:", "Nom-Nom-Nominal");
        telemetry.addData("Axes:", "Axial(%.2f), Lateral(%.2f), Yaw(%.2f)", axial, lateral, yaw);
        telemetry.addData("Wheels:", "Left(%.2f), Right(%.2f), Back(%.2f)", left, right, back);
        telemetry.addData("Brakes:", "Val(%.2f)", brakes);
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
