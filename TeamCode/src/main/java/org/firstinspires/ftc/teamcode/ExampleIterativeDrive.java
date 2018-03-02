package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Science Ring Bot V3.0", group="Iterative Opmode")

public class ExampleIterativeDrive extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armMotor = null;
    private Servo handServo = null;

    @Override
    public void init()
    {
        telemetry.addData("Status:", "Initialized");

        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");
        armMotor = hardwareMap.get(DcMotor.class, "armMotor");
        handServo = hardwareMap.get(Servo.class, "clawServo");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        handServo.setDirection(Servo.Direction.FORWARD);

        telemetry.addData("Status:", "Initialized");
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        armMotor.setPower(0.0);
        handServo.setPosition(0.0);

        telemetry.addData("Status:", "Awaiting Start");
    }

    @Override
    public void start()
    {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        leftDrive.setPower(-gamepad1.left_stick_y);
        rightDrive.setPower(-gamepad1.right_stick_y);
        armMotor.setPower(((gamepad1.right_trigger) - (gamepad1.left_trigger))/4);

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.getPosition() + 0.2 : handServo.getPosition() - 0.2);}
        catch (Exception ignored){handServo.setPosition(handServo.getPosition());}

        telemetry.addData("Status:", "Run Time: " + runtime.toString());
        telemetry.addData("Motors:", "left (%.2f), right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
        telemetry.addData("Arm:", "power (%.2f)", armMotor.getPower());
        telemetry.addData("Servo:", "position (%.2f)", handServo.getPosition());
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
