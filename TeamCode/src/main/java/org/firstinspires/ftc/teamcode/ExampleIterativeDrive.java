package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Example: Iterative OpMode", group="Iterative Opmode")

public class ExampleIterativeDrive extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
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

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        telemetry.addData("Status:", "Awaiting Start");
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop()
    {
        leftDrive.setPower(-gamepad1.left_stick_y);
        rightDrive.setPower(-gamepad1.right_stick_y);


        telemetry.addData("Status:", "Run Time: " + runtime.toString());
        telemetry.addData("Motors:", "left (%.2f), right (%.2f)", -gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        telemetry.addData("Status:", "Stopped");
    }
}
