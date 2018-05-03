package org.firstinspires.ftc.sciencecode.ThreeCorners;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import java.util.Objects;

@TeleOp(name="Three Corners Runner V2.0", group="Corners TeleOp")
public class ThreeCornersRun extends OpMode
{
    private NormalizedColorSensor colorSensor;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private double distance;

    private final double LEFT_ENCODER_RATE = 40.0;
    private final double RIGHT_ENCODER_RATE = 40.0;

    private String getThreeColor(NormalizedRGBA colors)
    {

        if (colors.red > 0.01 && colors.blue > 0.01 && colors.green > 0.01)
        {
            return "white";
        }
        else if (colors.red > colors.green && colors.red > colors.blue)
        {
            return "red";
        }
        else if (colors.blue > colors.green && colors.blue > colors.red)
        {
            return "blue";
        }
        return "white";
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
        rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);

        leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        distance = 0.0;

        if (colorSensor instanceof SwitchableLight)
        {
            ((SwitchableLight)colorSensor).enableLight(true);
        }
    }

    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
    }

    @Override
    public void start()
    {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        telemetry.addData("COLOR", this.getThreeColor(colors));
        switch (Objects.requireNonNull(this.getThreeColor(colors)))
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
                distance = 3000;
                telemetry.addData("Moving", "WHITE");
        }

        leftDrive.setTargetPosition((int) distance + leftDrive.getCurrentPosition());
        rightDrive.setTargetPosition((int) distance + rightDrive.getCurrentPosition());

        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(1);
        rightDrive.setPower(1);

        telemetry.update();
    }

    @Override
    public void loop()
    {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        telemetry.addData("Target Pos:", distance);
        telemetry.addData("Target Enc:", "R:" + distance * RIGHT_ENCODER_RATE + "\nL:" + distance * LEFT_ENCODER_RATE);

        telemetry.update();
    }
}
