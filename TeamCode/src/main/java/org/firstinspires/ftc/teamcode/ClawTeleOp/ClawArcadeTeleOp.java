package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Science Ring Bot Arcade V1.0", group="Iterative OpMode")
public class ClawArcadeTeleOp extends ClawTeleOpMain
{
    @Override
    public void loop()
    {
        double power = gamepad1.left_stick_y;

        try
        {
            leftDrive.setPower(gamepad2.b ? 0.0 : power * (1.0 - (gamepad1.left_stick_x)));
            rightDrive.setPower(gamepad2.b ? 0.0 : power * (-1.0 - (gamepad1.left_stick_x)));
            armMotor.setPower(gamepad2.b ? 0.0 : (-gamepad1.right_stick_y)/4);
        }
        catch (Exception ignored)
        {
            leftDrive.setPower(power * (1.0 - (gamepad1.left_stick_x)));
            rightDrive.setPower(power * (-1.0 - (gamepad1.left_stick_x)));
            armMotor.setPower((-gamepad1.left_stick_y)/4);
        }

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.MAX_POSITION : (gamepad1.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
        catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}

        telemetryStandard();
    }
}