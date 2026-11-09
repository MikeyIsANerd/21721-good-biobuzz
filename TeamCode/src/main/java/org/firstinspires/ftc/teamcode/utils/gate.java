package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class gate {

    public Servo g;

    public gate(HardwareMap hardwareMap){
        g = hardwareMap.get(Servo.class, "g");
    }

    public void open(){
        g.setPosition(1);
    }
    public void close(){
        g.setPosition(0);
    }

}