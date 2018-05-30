package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Controller Test V1.5", group="Iterative OpMode")
public class ControllerTest extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor armDrive = null;

    @Override
    public void init()
    {
        telemetry.addData("Status", "Initialized");

        this.leftDrive  = hardwareMap.get(DcMotor.class, "right_drive");
        this.rightDrive = hardwareMap.get(DcMotor.class, "left_drive");
        this.armDrive = hardwareMap.get(DcMotor.class, "arm_drive");

        this.leftDrive.setDirection(DcMotor.Direction.REVERSE);
        this.rightDrive.setDirection(DcMotor.Direction.FORWARD);
        this.armDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        this.leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop()
    {
        this.leftDrive.setPower(0.0);
        this.rightDrive.setPower(0.0);
        this.armDrive.setPower(0.0);
    }

    @Override
    public void start()
    {
        runtime.reset();
    }

    @Override
    public void loop()
    {
        this.leftDrive.setPower(-gamepad1.left_stick_y);
        this.rightDrive.setPower(-gamepad1.right_stick_y);

        this.armDrive.setPower(gamepad1.dpad_up ? 1.0 : (gamepad1.dpad_down ? -1.0 : 0.0));

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", this.leftDrive.getPower(), this.rightDrive.getPower());
    }

    @Override
    public void stop() {
    }
}
