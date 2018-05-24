package org.firstinspires.ftc.sciencecode.ThreeCorners;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import java.util.Objects;

@Autonomous(name="Challenge Colors Runner V3.4", group="Corners Auto")
public class ThreeCornersRun extends OpMode
{
    private NormalizedColorSensor colorSensor;
    private DcMotor leftDrive;
    private DcMotor rightDrive;
    private double distance;

    private final double LEFT_ENCODER_RATE = 100.0;
    private final double RIGHT_ENCODER_RATE = 100.0;

    private final double TURN_RATE = 5.0;

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

        leftDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        rightDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

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
        switch (Objects.requireNonNull(this.getThreeColor(colorSensor.getNormalizedColors())))
        {
            case "white":
                leftDrive.setTargetPosition((int) (leftDrive.getCurrentPosition() - (TURN_RATE * LEFT_ENCODER_RATE)));
                rightDrive.setTargetPosition((int) (rightDrive.getCurrentPosition() + (TURN_RATE * RIGHT_ENCODER_RATE)));
                distance = 100;
                telemetry.addData("Moving", "RED");
            case "blue":
                leftDrive.setTargetPosition((int) (leftDrive.getCurrentPosition() + (TURN_RATE * LEFT_ENCODER_RATE)));
                rightDrive.setTargetPosition((int) (leftDrive.getCurrentPosition() - (TURN_RATE * RIGHT_ENCODER_RATE)));
                distance = 100;
                telemetry.addData("Moving", "BLUE");
            case "red":
                distance = 140;
                telemetry.addData("Moving", "WHITE");
        }
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftDrive.setPower(0.64);
        rightDrive.setPower(1);
    }

    @Override
    public void loop()
    {
        if (leftDrive.getCurrentPosition() == leftDrive.getTargetPosition())
        {
            leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }

        if (leftDrive.getMode().equals(DcMotor.RunMode.STOP_AND_RESET_ENCODER))
        {
            leftDrive.setTargetPosition((int)((distance * LEFT_ENCODER_RATE) + leftDrive.getCurrentPosition()));
            rightDrive.setTargetPosition((int)((distance * RIGHT_ENCODER_RATE) + rightDrive.getCurrentPosition()));

            leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            leftDrive.setPower(0.64);
            rightDrive.setPower(1);
        }

        telemetry.addData("Target Pos:", distance);
        telemetry.addData("Target Enc:", "R:" + distance * RIGHT_ENCODER_RATE + "\tL:" + distance * LEFT_ENCODER_RATE);
        telemetry.addData("Current Pos:", "R:" + distance * RIGHT_ENCODER_RATE + "\tL:" + distance * LEFT_ENCODER_RATE);

        telemetry.update();
    }
}
