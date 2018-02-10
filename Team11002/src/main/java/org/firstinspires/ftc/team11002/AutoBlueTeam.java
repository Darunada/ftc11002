package org.firstinspires.ftc.team11002;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team11002.subassembly.ColorFlag;
import org.firstinspires.ftc.team11002.subassembly.StrafeDrive;
import org.firstinspires.ftc.team11002.subassembly.VerticalArm;

import java.util.concurrent.TimeUnit;

/**
 * Created by FRC on 2/7/2018.
 */

@Autonomous
public class AutoBlueTeam extends LinearOpMode {

    private boolean DEBUG_MODE = true;

    private StrafeDrive strafeDrive;
    private ColorFlag colorFlag;

    private ElapsedTime timer;
    private VerticalArm verticalArm;

    public AutoBlueTeam() {
        timer = new ElapsedTime();
    }

    private int red = 0;
    private int blue = 0;

    @Override
    public void runOpMode() {

        this.strafeDrive = new StrafeDrive(hardwareMap, telemetry, gamepad1, DEBUG_MODE);
        this.colorFlag = new ColorFlag(hardwareMap, telemetry, gamepad1, DEBUG_MODE);
        this.verticalArm = new VerticalArm(hardwareMap, telemetry, gamepad1, DEBUG_MODE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        timer.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            long dT = timer.time(TimeUnit.SECONDS);

            if(dT < 3) {
                // color_flag extends
                colorFlag.setPosition(1.0d);
                colorFlag.startColorSensor();
            } else if(dT < 5) {
                // wait?
                telemetry.addData("Count to 5!", "Count to 5: "+dT);
                colorFlag.makeColorReading();

                if (colorFlag.seesRed()) {
                    red++;
                } else if (colorFlag.seesBlue()) {
                    blue++;
                }

            } else if(dT < 6) {

                if ( red < blue) {
                    strafeDrive.strafeRight();
                } else if (blue < red) {
                    strafeDrive.strafeLeft();
                } else {
                    // do
                    // nothing
                }


            } else {
                // stand by
                strafeDrive.reset();
                colorFlag.reset();
            }

            telemetry.update();
        }
    }


}
