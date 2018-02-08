package org.firstinspires.ftc.team11002.subassembly;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by FRC on 2/7/2018.
 */

public class VerticalArm {

    private boolean DEBUG_MODE = false;


    private String debugCaption = "verticalArm";

    private DcMotor arm_drive;
    private Servo claw_servo;

    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    /** Claw Config */
    private boolean clawToggleReady = true;
    private double servoPosition = -1;
    private double servoLocation = 0;
    private double MIN = 0.75d;
    private double MAX = 1.0d;

    /** Arm Config */
    private double armPowerMultiplier = 0.93d;
    private double ARM_MAX_DOWN = -0.9d;
    private double ARM_MAX_UP = 0.9d;

    /**
     * Init with debug mode off
     * @param hardwareMap
     * @param telemetry
     */
    public VerticalArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        init(hardwareMap, telemetry, gamepad, false);
    }

    /**
     * Init with debug mode configured
     * @param hardwareMap
     * @param telemetry
     * @param debug
     */
    public VerticalArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean debug) {
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

        arm_drive = hardwareMap.get(DcMotor.class, "arm_drive");
        arm_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw_servo = hardwareMap.get(Servo.class, "claw_servo");
        claw_servo.setPosition(0.5d);

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

            if(forceUpdate) {
                telemetry.update();
            }
        }
    }


    /**
     *
     */
    public void realtimeArm() {

        double armPower = gamepad.right_stick_y * armPowerMultiplier;
        if(armPower < ARM_MAX_DOWN) {
            armPower = ARM_MAX_DOWN;
        } else if(armPower > ARM_MAX_UP) {
            armPower = ARM_MAX_UP;
        }
        arm_drive.setPower(armPower);

        logTelemetry("arm power" + armPower);

        realtimeClaw();
    }

    /**
     *
     */
    private void realtimeClaw() {
        if(clawToggleReady && gamepad.b) {
            clawToggleReady = false;
            servoPosition = servoPosition * -1;
        } else if(!gamepad.b) {
            clawToggleReady = true;
        }

        if(servoPosition < MIN) {
            servoLocation = MIN;
        } else if(servoPosition > MAX) {
            servoLocation = MAX;
        } else {
            servoLocation = servoPosition;
        }

        claw_servo.setPosition(servoLocation);
        logTelemetry("toggle ready" + clawToggleReady + " location:" + servoLocation);
    }
}
