/*
The purpose of this program is to find the PIDF values that would bring up our flywheel
motor to a speed quickly and keep it there.

In auto, we could declare the motor in the init method with the tuned PIDF values,
but I think in tele-op we would need a set of values for shooting from each loading zone
with the ability to switch between them.

To elaborate, in auto, rather than starting the program with PF of 0 like below,
you’d start with the tuned values.

When finding the values for a desired velocity, find F first, then P at your given F.
 */

package org.firstinspires.ftc.teamcode.utils;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
/* Functionality of DcMotorEx: All DcMotor features plus advanced
   methods like setVelocity(), getVelocity(), setPIDCoefficients(),
   and current sensing.
 */
// import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class PIDTune extends OpMode {
    public DcMotorEx flywheel;

    public double highVelocity = 1500;
    public double lowVelocity = 900;

    double currentTargetVelocity = 1500;

    // F = feedforward/power prediction; how much power you think you need to maintain your
    // current speed
    double F = 0;
    // P= power; the power being sent to the motor; the farther away from the target velocity,
    // the more power you want to send; the closer you are, the less power you want to send.
    // most important constant to fine-tune/control
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIndex =1;


    @Override
    public void init(){
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel");
        flywheel.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheel.setDirection(DcMotorSimple.Direction.FORWARD);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0,0, F);
        flywheel.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");

    }

    @Override
    public void loop(){
        // toggle between lowVelocity and highVelocity
        if (gamepad2.yWasPressed()) {
            if (currentTargetVelocity == highVelocity){
                currentTargetVelocity = lowVelocity;
            }
            else {
                currentTargetVelocity = highVelocity;
            }
        }

        // change step size
        if (gamepad2.bWasPressed()) {
            stepIndex = (stepIndex + 1 % stepSizes.length);
        }

        // update P and F values on the fly; F: right to increase, left to decrease
        // P: up to increase, down to decrease
        if (gamepad2.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }
        if (gamepad2.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad2.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad2.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }
        // set new P, F coefficients
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0,0, F);
        flywheel.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // set target velocity
        flywheel.setVelocity(currentTargetVelocity);

        // get measurements for telemetry: current velocity, and error, which is how far away
        // we are from reaching our target velocity
        double curVelocity = flywheel.getVelocity();
        double error = currentTargetVelocity - curVelocity;

        //telemetry
        telemetry.addData("Target velocity: ", currentTargetVelocity);
        telemetry.addData("Current velocity: ", "%.2f", curVelocity);
        telemetry.addData("Error: ", "%.2f", error);
        telemetry.addLine("----------------------------");
        telemetry.addData("Tuning P: ", "%.4f (D-pad U/D)", P);
        telemetry.addData("Tuning F: ", "%.4f (D-pad L/R)", F);
        telemetry.addData("Step size: ", "%.4f (B button)", stepSizes[stepIndex]);
    }
}