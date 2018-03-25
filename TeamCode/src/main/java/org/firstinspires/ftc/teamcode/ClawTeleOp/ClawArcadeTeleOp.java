package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Science Ring Bot Arcade V3.0", group="Iterative OpMode Claw")
public class ClawArcadeTeleOp extends ClawTeleOpMain
{
    @Override
    public void loop()
    {
        double power = Range.clip((Math.sqrt(Math.pow(gamepad1.left_stick_x, 2) + Math.pow(gamepad1.left_stick_y, 2))), -1, 1) * (gamepad1.right_stick_y >= 0 ? 1: -1);

        try
        {
            leftDrive.setPower(gamepad2.b ? 0.0 : power * (1.0 - (-gamepad1.left_stick_x)));
            rightDrive.setPower(gamepad2.b ? 0.0 : power * (1.0 - (gamepad1.left_stick_x)));
            armDrive.setPower(gamepad2.b ? 0.0 : (-gamepad1.right_stick_y)/4);
        }
        catch (Exception ignored)
        {
            leftDrive.setPower(Range.clip((power * (1.0 - (-gamepad1.left_stick_x))), -1, 1));
            rightDrive.setPower(Range.clip((power * (1.0 - (gamepad1.left_stick_x))), -1, 1));
            armDrive.setPower((-gamepad1.left_stick_y)/4);
        }

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.MAX_POSITION : (gamepad1.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
        catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}

        telemetryStandard();
    }
}