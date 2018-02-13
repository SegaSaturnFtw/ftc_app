package org.firstinspires.ftc.team6188;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Example: Iterative OpMode Test", group="Iterative Opmode")

public class ExampleIterativeDrive extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    @Override //runs once when the driver hits init
    public void init() {
        telemetry.addData("Status:", "Initialized");

        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        telemetry.addData("Status:", "Initialized");
    }

    @Override //code to run repeatedly after the driver hits INIT and before hits play
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        telemetry.addData("Status:", "Awaiting Start");
    }

    @Override //runs once when the driver hits play
    public void start()
    {
        runtime.reset();
    }

    @Override //runs repeatedly after driver hits play
    public void loop()
    {
        leftDrive.setPower(-gamepad1.left_stick_y);
        rightDrive.setPower(-gamepad1.right_stick_y);

        telemetry.addData("Status:", "Run Time: " + runtime.toString());
        telemetry.addData("Motors:", "left (%.2f), right (%.2f)", -gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    @Override //runs once after the driver hits stop
    public void stop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        telemetry.addData("Status:", "Stopped");
        telemetry.addData("Status:", "Total Run Time: " + runtime.toString());
    }
}
