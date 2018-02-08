package org.firstinspires.ftc.team11002.subassembly;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by FRC on 2/7/2018.
 */

public class StrafeDrive {

    private boolean DEBUG_MODE = false;
    private String debugCaption = "strafeDrive";

    private DcMotor right_drive;
    private DcMotor left_drive;
    private DcMotor strafe_drive;

    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    /**
     * Init with debug mode off
     * @param hardwareMap
     * @param telemetry
     */
    public StrafeDrive(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        init(hardwareMap, telemetry, gamepad, false);
    }

    /**
     * Init with debug mode configured
     * @param hardwareMap
     * @param telemetry
     * @param debug
     */
    public StrafeDrive(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean debug) {
        init(hardwareMap, telemetry, gamepad, debug);
    }

    /**
     *
     * @param hardwareMap
     * @param telemetry
     * @param debug
     */
    protected void init(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean debug) {
        DEBUG_MODE = debug;
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad = gamepad;

        logTelemetry("Initializing...", true);

        right_drive = hardwareMap.get(DcMotor.class, "right_drive");
        left_drive = hardwareMap.get(DcMotor.class, "left_drive");
        strafe_drive = hardwareMap.get(DcMotor.class, "strafe_drive");

        left_drive.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        right_drive.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors


        logTelemetry("Started!", true);

    }

    /**
     *
     * @param message
     */
    protected void logTelemetry(String message) {
        this.logTelemetry(message, false);
    }

    /**
     *
     * @param message
     * @param forceUpdate
     */
    protected void logTelemetry(String message, boolean forceUpdate) {
        if(DEBUG_MODE) {
            telemetry.addData(debugCaption, message);
            telemetry.update();
        }
    }


    /**
     *
     */
    public void realtimeDrive() {
        // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
        // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
        // This way it's also easy to just drive straight, or just turn.
        double drive = -gamepad.left_stick_y;
        double turn = gamepad.right_stick_x;

        // Combine drive and turn for blended motion.
        double left = drive - turn;
        double right = drive + turn;
        double strafe = gamepad.left_stick_x;

        // Normalize the values so neither exceed +/- 1.0
        double max = Math.max(Math.abs(left), Math.abs(right));
        if (max > 1.0) {
            left /= max;
            right /= max;
        }

        left_drive.setPower(left);
        right_drive.setPower(right);
        strafe_drive.setPower(strafe);

        logTelemetry("drive: " + drive + "turn: " + turn + "left: " + left + " right:" + right + " strafe:" + strafe);
    }
}
