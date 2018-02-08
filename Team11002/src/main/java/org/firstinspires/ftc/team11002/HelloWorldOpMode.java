package org.firstinspires.ftc.team11002;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team11002.subassembly.StrafeDrive;
import org.firstinspires.ftc.team11002.subassembly.VerticalArm;

/**
 * Created by FRC on 2/7/2018.
 */

@TeleOp
public class HelloWorldOpMode extends LinearOpMode {

    private boolean DEBUG_MODE = true;

    private StrafeDrive strafeDrive;
    private VerticalArm verticalArm;

    @Override
    public void runOpMode() {

        this.strafeDrive = new StrafeDrive(hardwareMap, telemetry, gamepad1, DEBUG_MODE);
        this.verticalArm = new VerticalArm(hardwareMap, telemetry, gamepad1, DEBUG_MODE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            this.strafeDrive.realtimeDrive();
            this.verticalArm.realtimeArm();

            telemetry.update();
        }
    }


}
