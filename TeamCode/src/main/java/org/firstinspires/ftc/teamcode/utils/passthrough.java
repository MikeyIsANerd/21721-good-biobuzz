package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class passthrough {
    public static double pow = (.5);




    public static DcMotor t;



    public passthrough(HardwareMap hardwareMap) {
        t = hardwareMap.get(DcMotor.class, "t");


    }



    public static void in(double x){
        t.setPower(x);

    }


    public static void off(){
        t.setPower(0);

    }


}