package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Science Ring Bot Arcade V1.0", group="Iterative OpMode")
public class ClawArcadeTeleOpMain extends ClawTeleOpMain
{
    @Override
    public void loop()
    {
        double power = gamepad1.left_stick_y;

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

        telemetry.addData("Joysticks P1:", "Left (%.2f), Right (%.2f)", gamepad1.left_stick_y, gamepad1.right_stick_y);
        telemetry.addData("Motors:", "Left (%.2f), Right (%.2f)", leftDrive.getPower(), rightDrive.getPower());
        telemetry.addData("Arm:", "Power (%.2f)", armMotor.getPower());
        telemetry.addData("Servo:", "Position (%.2f)", handServo.getPosition());

        try{telemetry.addData("P2 Status:", "Active: " + ((gamepad2.b) ? "Breaking" : "Passive"));}
        catch (Exception ignored){telemetry.addData("P2 Status:", "Disabled");}
    }
}