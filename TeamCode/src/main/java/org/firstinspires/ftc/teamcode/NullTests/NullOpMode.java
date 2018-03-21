package org.firstinspires.ftc.teamcode.NullTests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Null Test OpMode V1.0", group="Iterative OpMode")
public class NullOpMode extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init()
    {
        telemetry.addData("Status:", "Initialized");
    }

    @Override
    public void start()
    {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        telemetry.addData("JoySticks:", "LeftY: (%.2f), RightY: (%.2f), LeftY: (%.2f), RightY: (%.2f)",
                gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_y, gamepad1.right_stick_x);
    }
}