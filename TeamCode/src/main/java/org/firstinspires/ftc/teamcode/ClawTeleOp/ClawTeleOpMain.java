package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Default Claw Tele Op V2.0", group="Iterative OpMode")
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
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        handServo = hardwareMap.get(Servo.class, "clawServo");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        handServo.setDirection(Servo.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        handServo.scaleRange(0.2, 1.0);

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
    public void start()
    {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        try
        {
            leftDrive.setPower(gamepad2.b ? 0.0 : ((Math.pow(gamepad1.left_stick_y, 2) * (gamepad1.left_stick_y / 2)) * 1.6));
            rightDrive.setPower(gamepad2.b ? 0.0 : ((Math.pow(gamepad1.right_stick_y, 2) * (gamepad1.right_stick_y / 2)) * 1.6));
            armMotor.setPower(gamepad2.b ? 0.0 : ((gamepad1.right_trigger) - (gamepad1.left_trigger)) / 4);
        }
        catch (Exception ignored)
        {
            leftDrive.setPower((Math.pow(gamepad1.left_stick_y, 2) * (gamepad1.left_stick_y / 2)) * 1.6);
            rightDrive.setPower((Math.pow(gamepad1.right_stick_y, 2) * (gamepad1.right_stick_y / 2)) * 1.6);
            armMotor.setPower(((gamepad1.right_trigger) - (gamepad1.left_trigger)) / 4);
        }

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.MAX_POSITION : (gamepad1.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
        catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}

        telemetryStandard();
    }

    void telemetryStandard()
    {
        telemetry.addData("Status:", "Running Nominally: " + runtime.toString());
        telemetry.addData("Motors:", "Left (%.2f), Right (%.2f), Arm (%.2f)", leftDrive.getPower(), rightDrive.getPower(), armMotor.getPower());
        telemetry.addData("Servo:", "Position (%.2f)", handServo.getPosition());
        telemetry.addData("Joysticks P1:", "Left (%.2f), Right (%.2f)", gamepad1.left_stick_y, gamepad1.right_stick_y);

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