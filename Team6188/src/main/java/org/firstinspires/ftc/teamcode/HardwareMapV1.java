package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

public class HardwareMapV1
{
    public DcMotor driveFR = null;
    public DcMotor driveFL = null;
    public DcMotor driveBR = null;
    public DcMotor driveBL = null;

    private HardwareMap hwMap = null;
    private ElapsedTime elapsedTime = new ElapsedTime();

    public void init(HardwareMap awhMP)
    {
        hwMap = awhMP;
        driveBR = hwMap.get(DcMotor.class, "driveBR");
        driveBL = hwMap.get(DcMotor.class, "driveBL");
        driveFR = hwMap.get(DcMotor.class, "driveFR");
        driveFL = hwMap.get(DcMotor.class, "driveFL");
    }
}
