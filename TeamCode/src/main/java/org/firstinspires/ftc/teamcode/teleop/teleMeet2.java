
package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.gate;
import org.firstinspires.ftc.teamcode.utils.intake;
import org.firstinspires.ftc.teamcode.utils.passthrough;
import org.firstinspires.ftc.teamcode.utils.shooterV1;

@TeleOp(name="meet 2 tele", group="OpMode")
public class teleMeet2 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backRightDrive = null;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        frontLeftDrive = hardwareMap.get(DcMotor.class, "fl");
        backLeftDrive = hardwareMap.get(DcMotor.class, "bl");
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr");
        backRightDrive = hardwareMap.get(DcMotor.class, "br");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);

        shooterV1 s = new shooterV1(hardwareMap);
        passthrough p = new passthrough(hardwareMap);
        intake i = new intake(hardwareMap);
        gate g = new gate(hardwareMap);

        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            s.run();
            double max;
            double joy = gamepad2.right_trigger;
            double joyTwo = -gamepad2.left_trigger;


            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double frontLeftPower  = axial - lateral - yaw;
            double frontRightPower = axial + lateral + yaw;
            double backLeftPower   = axial + lateral - yaw;
            double backRightPower  = axial - lateral + yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
            max = Math.max(max, Math.abs(backLeftPower));
            max = Math.max(max, Math.abs(backRightPower));

            if (max > 1.0) {
                frontLeftPower  /= max;
                frontRightPower /= max;
                backLeftPower   /= max;
                backRightPower  /= max;
            }

            if (joy > 0.1){
                intake.in(joy);
                p.in(joy*.8);

            }
            else if(!(joy > 0.1)){
                intake.in(joyTwo);
                p.in(joyTwo*.8);;
            }

            if(gamepad2.right_bumper)
                g.open();
            else
                g.close();




            frontLeftDrive.setPower(frontLeftPower * .8);
            frontRightDrive.setPower(frontRightPower * .8);
            backLeftDrive.setPower(backLeftPower * .8);
            backRightDrive.setPower(backRightPower * .8);

            // Show the elapsed game time and wheel power.
            telemetry.addData("current RPM", s.getChipRPM());
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontLeftPower, frontRightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backLeftPower, backRightPower);
            telemetry.update();
        }
    }}