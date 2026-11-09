package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class intake {
    public static double intakePower = (-1);
    public static double servoPower = (1);

    public static double intakePowerOut = (1);





    public static DcMotor intakeFront;
    public static CRServo iL;
    public static CRServo iR;



    public intake(HardwareMap hardwareMap) {
        intakeFront = hardwareMap.get(DcMotor.class, "in");
        //iL = hardwareMap.get(CRServo.class, "inL");
        //iR = hardwareMap.get(CRServo.class, "inR");

        intakeFront.setDirection(DcMotor.Direction.FORWARD);

    }



    public static void in(double x){
        //iL.setPower(x);
        //iR.setPower(x);
        intakeFront.setPower(x);
    }

    public static void out(double x){
        //iL.setPower(-x);
        //iR.setPower(-x);
        intakeFront.setPower(-x);
    }






}