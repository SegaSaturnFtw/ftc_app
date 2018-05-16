package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Controller Test V1.0", group="Iterative OpMode")
public class ControllerTest extends OpMode
{
    @Override
    public void init()
    {
        telemetry.addData("Init", "Initialized");
    }

    @Override
    public void loop()
    {
        telemetry.addData("Stick-L", "X:\t" + gamepad1.left_stick_x + "\tY:\t" + gamepad1.left_stick_y);
        telemetry.addData("Stick-R", "X:\t" + gamepad1.right_stick_x + "\tY:\t" + gamepad1.right_stick_y);

        telemetry.update();
    }
}
