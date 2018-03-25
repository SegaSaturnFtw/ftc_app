package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Science Ring Bot Vehicle V1.0", group="Iterative OpMode")
public class ClawVehicleTeleOp extends ClawTeleOpMain
{
    @Override
    public void loop()
    {
        double power = gamepad1.right_trigger - gamepad1.left_trigger;

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
}