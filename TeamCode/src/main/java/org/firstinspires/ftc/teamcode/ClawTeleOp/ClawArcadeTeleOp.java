package org.firstinspires.ftc.teamcode.ClawTeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Science Ring Bot Arcade V4.2", group="Iterative OpMode Claw")
public class ClawArcadeTeleOp extends ClawTeleOpMain
{
    @Override
    public void loop()
    {
        double vertical = (((Math.pow(gamepad1.left_stick_y, 2) * (-gamepad1.left_stick_y / 2)) * 0.75));
        double horizontal = (((Math.pow(gamepad1.left_stick_x, 2) * (-gamepad1.left_stick_x / 2)) * 0.5));

        try
        {
            leftDrive.setPower(gamepad2.b ? 0.0 : Range.clip(vertical - horizontal, -1, 1));
            rightDrive.setPower(gamepad2.b ? 0.0 : Range.clip(vertical + horizontal, -1, 1));
            armDrive.setPower(gamepad2.b ? 0.0 : (-gamepad1.right_stick_y)/4);
        }
        catch (Exception ignored)
        {
            leftDrive.setPower(Range.clip(vertical - horizontal, -1, 1));
            rightDrive.setPower(Range.clip(vertical + horizontal, -1, 1));
            armDrive.setPower((-gamepad1.left_stick_y)/4);
        }

        try {handServo.setPosition(gamepad1.left_bumper ? handServo.MAX_POSITION : (gamepad1.right_bumper ? handServo.MIN_POSITION : handServo.getPosition()));}
        catch (Exception ignored) {handServo.setPosition(handServo.getPosition());}

        telemetryStandard();
    }
}