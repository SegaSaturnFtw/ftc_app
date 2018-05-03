package org.firstinspires.ftc.sciencecode.ThreeCorners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import java.util.Objects;

@TeleOp(name="Color Calibration V1.4", group="Corners TeleOp")
public class ColorCalibration extends OpMode
{
    private NormalizedColorSensor colorSensor;

    @Override
    public void init()
    {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");

        if (colorSensor instanceof SwitchableLight)
        {
            ((SwitchableLight)colorSensor).enableLight(true);
        }
    }

    @Override
    public void loop()
    {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        if (colors.red > 0.01 && colors.blue > 0.01 && colors.green > 0.01)
        {
            telemetry.addData("WHITE", "WHITE");
        }
        else if (colors.red > colors.green && colors.red > colors.blue)
        {
            telemetry.addData("RED", "RED");
        }
        else if (colors.blue > colors.green && colors.blue > colors.red)
        {
            telemetry.addData("BLUE", "BLUE");
        }

        telemetry.addData("Color", "R: " + colors.red + "\nG: " + colors.green + "\nB: " + colors.blue);
        telemetry.update();
    }
}
