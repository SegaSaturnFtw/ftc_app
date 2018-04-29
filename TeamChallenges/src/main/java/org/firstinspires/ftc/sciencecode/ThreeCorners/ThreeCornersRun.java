package org.firstinspires.ftc.sciencecode.ThreeCorners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@TeleOp(name="Three Corners Runner V1.1", group="Corners TeleOp")
public class ThreeCornersRun extends OpMode
{
    private NormalizedColorSensor colorSensor;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private double distance;
    private final double ENCODER_RATE = 1.0;

    private String getThreeColor(NormalizedRGBA color)
    {
        if (color.red > 0.02 && color.blue > 0.02)
        {
            return "white";
        }
        else if (color.red > color.green && color.red > color.blue)
        {
            return "red";
        }
        else if (color.blue > color.green && color.blue > color.red)
        {
            return "blue";
        }
    }

    @Override
    public void init()
    {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        distance = 0.0;

        if (colorSensor instanceof SwitchableLight)
        {
            ((SwitchableLight)colorSensor).enableLight(true);
        }
    }

    @Override
    public void loop()
    {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        if (distance == 0.0)
        {
            telemetry.addData("COLOR", this.getThreeColor(colors));
            switch (this.getThreeColor(colors))
            {
                case "red":
                    //move left
                    distance = 2000;
                    telemetry.addData("Moving", "RED");
                case "blue":
                    //move right
                    distance = 2000;
                    telemetry.addData("Moving", "BLUE");
                case "white":
                    //move forward
                    distance = 8000;
                    telemetry.addData("Moving", "WHITE");
            }
            telemetry.addData("DISTANCE", distance);
            telemetry.update();
        }

        telemetry.addData("Target Pos:", distance);
        telemetry.addData("Target Motor:", (int) (distance * ENCODER_RATE));
        telemetry.addData("Distance Left:", (distance - (int)(distance * ENCODER_RATE)));

        leftDrive.setTargetPosition((int) (distance * ENCODER_RATE));
        leftDrive.setTargetPosition((int) (distance * ENCODER_RATE));

        leftDrive.setPower(100);
        rightDrive.setPower(100);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        telemetry.update();
    }
}
