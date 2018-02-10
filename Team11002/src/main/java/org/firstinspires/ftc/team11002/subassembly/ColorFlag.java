package org.firstinspires.ftc.team11002.subassembly;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by FRC on 2/7/2018.
 */

public class ColorFlag {

    private boolean DEBUG_MODE = false;
    private String debugCaption = "colorFlag";

    /**
     * Config
     * 0 is all the way up
     * 1 is all the way down
     * 0.4 puts the flag vertical
     */
    private double INITIAL_POSITION = 0.4;

    private Servo flagServo;
    private ColorSensor flagSensor;

    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    /**
     * Init with debug mode off
     * @param hardwareMap
     * @param telemetry
     */
    public ColorFlag(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        init(hardwareMap, telemetry, gamepad, false);
    }

    /**
     * Init with debug mode configured
     * @param hardwareMap
     * @param telemetry
     * @param debug
     */
    public ColorFlag(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean debug) {
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

        flagServo = hardwareMap.get(Servo.class, "flag_servo");
        flagSensor = hardwareMap.get(ColorSensor.class, "color_sensor");
        this.reset();


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
     * allows realtime control of the flag subsystem
     */
    public void realtimeFlag() {
        // do
        // nothing
    }

    /**
     *
     * @param position
     */
    public void setPosition(double position) {
        this.flagServo.setPosition(position);
    }

    /**
     *
     */
    public void startColorSensor() {
        this.flagSensor.enableLed(true);
    }

    public void makeColorReading() {
        this.logTelemetry("Red: "+ flagSensor.red() +" Green: "+ flagSensor.green() +" Blue:"+ flagSensor.blue(), false);
    }

    public boolean seesRed() {
        int red = this.flagSensor.red();
        int blue = this.flagSensor.blue();

        return red > blue;
    }

    public boolean seesBlue() {
        int red = this.flagSensor.red();
        int blue = this.flagSensor.blue();

        return blue > red;
    }

    public void reset() {
        flagServo.setPosition(INITIAL_POSITION);
        flagSensor.enableLed(false);
    }
}
