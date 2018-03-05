package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Science Ring Bot Vehicle V1.4", group="Iterative Opmode")

public class ClawBotArcadeTeleOp extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armMotor = null;
    private Servo handServo = null;

    private boolean testMode = false;

    @Override
    public void init()
    {
        telemetry.addData("Status:", "Initialized");

        try
        {
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

            telemetry.addData("Mode:", "Drive");
        }
        catch (Exception ignored)
        {
            testMode = true;
            telemetry.addData("Mode:", "Test");
        }

        telemetry.addData("Status:", "Initialized");
    }

    @Override
    public void init_loop()
    {
        if (!testMode)
        {
            leftDrive.setPower(0.0);
            rightDrive.setPower(0.0);
            armMotor.setPower(0.0);
            handServo.setPosition(handServo.MAX_POSITION);
        }

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
        if (!testMode)
        {
            double power = gamepad1.right_trigger - gamepad1.left_trigger;

            leftDrive.setPower(gamepad1.left_stick_x > 0 ? power : power * (1.0 - gamepad1.left_stick_x));
            rightDrive.setPower(gamepad1.left_stick_x < 0 ? power : power * (1.0 + gamepad1.left_stick_x));

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

            telemetry.addData("Status:", "Run Time: " + runtime.toString());
            telemetry.addData("Motors:", "left (%.2f), right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
            telemetry.addData("Arm:", "power (%.2f)", armMotor.getPower());
            telemetry.addData("Servo:", "position (%.2f)", handServo.getPosition());
        }
        else
        {
            double power = gamepad1.right_trigger - gamepad1.left_trigger;
            double leftPower = (gamepad1.left_stick_x > 0 ? power : power * (1.0 - -gamepad1.left_stick_x));
            double rightPower = (gamepad1.left_stick_x < 0 ? power : power * (1.0 - gamepad1.left_stick_x));

            try
            {
                if (gamepad2.b)
                {
                    leftDrive.setPower(0.0);
                    rightDrive.setPower(0.0);
                    armMotor.setPower(0.0);
                }
            }
            catch (Exception ignored){}

            telemetry.addData("Status:", "Run Time: " + runtime.toString());
            telemetry.addData("Motors:", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("GamePad::", "Left (%.2f), Right (%.2f)", gamepad1.left_stick_y, gamepad1.right_stick_y);
        }
    }

    @Override
    public void stop()
    {
        if (!testMode)
        {
            leftDrive.setPower(0.0);
            rightDrive.setPower(0.0);
            armMotor.setPower(0.0);
            handServo.setPosition(handServo.getPosition());
        }

        telemetry.addData("Status:", "Stopped");
    }
}
