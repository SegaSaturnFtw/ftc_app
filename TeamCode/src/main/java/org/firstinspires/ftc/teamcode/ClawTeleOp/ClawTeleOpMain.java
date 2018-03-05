package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Default Claw Tele Op", group="Iterative OpMode")
@Disabled
public class ClawTeleOpMain extends OpMode
{
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    DcMotor armMotor = null;
    Servo handServo = null;

    @Override
    public void init()
    {
        telemetry.addData("Status:", "Initialized");

        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        handServo = hardwareMap.get(Servo.class, "clawServo");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        handServo.setDirection(Servo.Direction.FORWARD);

        handServo.scaleRange(0.1, 1.0);

        telemetry.addData("Status:", "Initialized");
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        armMotor.setPower(0.0);
        handServo.setPosition(handServo.MAX_POSITION);

        telemetry.addData("Status:", "Awaiting Start | Vehicle");
    }

    @Override
    public void loop()
    {
        leftDrive.setPower((Math.pow(gamepad1.left_stick_y, 2) * (gamepad1.left_stick_y / 2)) * 1.6);
        rightDrive.setPower((Math.pow(gamepad1.right_stick_y, 2) * (gamepad1.right_stick_y / 2)) * 1.6);
        armMotor.setPower(((gamepad1.right_trigger) - (gamepad1.left_trigger)) / 4);

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.MAX_POSITION : (gamepad1.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
        catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}

        try
        {
            if (gamepad2.b)
            {
                leftDrive.setPower(0.0);
                rightDrive.setPower(0.0);
                armMotor.setPower(0.0);
            }

            try {handServo.setPosition(gamepad2.left_bumper ? handServo.MAX_POSITION : (gamepad2.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
            catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}
        }
        catch (Exception ignored) {}

        telemetry.addData("Joysticks P1:", "Left (%.2f), Right (%.2f)", gamepad1.left_stick_y, gamepad1.right_stick_y);
        telemetry.addData("Motors:", "Left (%.2f), Right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
        telemetry.addData("Arm:", "Power (%.2f)", armMotor.getPower());
        telemetry.addData("Servo:", "Position (%.2f)", handServo.getPosition());

        try{telemetry.addData("P2 Status:", "Active: " + ((gamepad2.b) ? "Breaking" : "Passive"));}
        catch (Exception ignored){telemetry.addData("P2 Status:", "Disabled");}
    }

    @Override
    public void stop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        armMotor.setPower(0.0);
        handServo.setPosition(handServo.getPosition());

        telemetry.addData("Status:", "Stopped");
    }
}