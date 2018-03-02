package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TriBot Iterative V1.0 (TEST DEV)", group="Iterative Opmode")

public class TriBotTeleOp extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor backDrive = null;
    private Servo armServo = null;

    @Override
    public void init()
    {
        leftDrive = hardwareMap.get(DcMotor.class, "left drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right drive");
        backDrive = hardwareMap.get(DcMotor.class, "back drive");
        armServo = hardwareMap.get(Servo.class, "claw arm");

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        backDrive.setDirection(DcMotor.Direction.FORWARD);

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
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        double axial = Range.clip((-gamepad1.left_stick_y/4), -1, 1);
        double lateral = (Range.clip((-gamepad1.left_stick_x/4), -1, 1)) * 0.5;
        double yaw = Range.clip((-gamepad1.right_stick_x/4), -1, 1);

        double back = yaw + lateral;
        double left = yaw - axial - lateral;
        double right = yaw + axial - lateral;

        double max = Math.max(Math.max(Math.abs(left), Math.abs(right)), Math.abs(back));
        if (max > 0.1)
        {
            back /= max;
            left /= max;
            right /= max;
        }

        backDrive.setPower(back);
        leftDrive.setPower(left);
        rightDrive.setPower(right);

        try{armServo.setPosition(gamepad1.left_bumper ? armServo.getPosition() - 0.2 : (gamepad1.right_bumper ? (armServo.getPosition() + 0.2)/4 : armServo.getPosition()));}
        catch (Exception ignored) {armServo.setPosition(armServo.getPosition());}

        telemetry.addData("Axes:", "Axial[%+5.2f], Lateral[%+5.2f], Yaw[%+5.2f]", axial, lateral, yaw);
        telemetry.addData("Wheels:", "Left[%+5.2f], Right[%+5.2f], Back[%+5.2f]", left, right, back);
        telemetry.addData("Servo:", "Pos[%.2f]", armServo.getPosition());
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
