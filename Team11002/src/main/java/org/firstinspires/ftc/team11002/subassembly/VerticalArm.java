package org.firstinspires.ftc.team11002.subassembly;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by FRC on 2/7/2018.
 */

public class VerticalArm {

    private boolean DEBUG_MODE = false;

    private boolean ARM_ENCODER_LIMITS = false;
    private int ARM_ENCODER_TOP = 0;
    private int ARM_ENCODER_BOTTOM = 0;
    private int arm_target;

    private String debugCaption = "verticalArm";

    private DcMotor arm_drive;
    private TouchSensor arm_limit;
    private Servo claw_servo;

    private Telemetry telemetry;
    private HardwareMap hardwareMap;
    private Gamepad gamepad;

    /**
     * Claw Config
     */
    private boolean clawToggleReady = true;
    private double servoPosition = -1;
    private double servoLocation = 0;
    private double MIN = 0.75d;
    private double MAX = 1.0d;

    /**
     * Arm Config
     */
    private double armPowerMultiplier = 1.0d;
    private double ARM_MAX_DOWN = -1.0d;
    private double ARM_MAX_UP = 1.0d;
    private int ARM_ENCODER_SPAN = 11000;

    /**
     * Init with debug mode off
     *
     * @param hardwareMap
     * @param telemetry
     */
    public VerticalArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad) {
        init(hardwareMap, telemetry, gamepad, false);
    }

    /**
     * Init with debug mode configured
     *
     * @param hardwareMap
     * @param telemetry
     * @param debug
     */
    public VerticalArm(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad, boolean debug) {
        init(hardwareMap, telemetry, gamepad, debug);
    }

    /**
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

        arm_limit = hardwareMap.get(TouchSensor.class, "arm_limit");
        arm_drive = hardwareMap.get(DcMotor.class, "arm_drive");
        arm_drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // find home (the bottom)
//        while (!arm_limit.isPressed()) {
//            arm_drive.setPower(ARM_MAX_DOWN);
//            try {
//                Thread.sleep(30);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
        arm_drive.setPower(0);

//        if(arm_limit.isPressed()) {
            // ATTENTION THE SWITCH IS BROKE SO I DISABLED THE CHECK.
            // START THE ROBOT AT THE BOTTOM OF ITS THROW
            this.ARM_ENCODER_BOTTOM = arm_drive.getCurrentPosition();
            this.ARM_ENCODER_TOP = arm_drive.getCurrentPosition() + ARM_ENCODER_SPAN;
            this.ARM_ENCODER_LIMITS = true;
//        }

        claw_servo = hardwareMap.get(Servo.class, "claw_servo");
        claw_servo.setPosition(0.5d);

        logTelemetry("Started!", true);

    }

    /**
     * @param message
     */
    protected void logTelemetry(String message) {
        this.logTelemetry(message, false);
    }

    /**
     * @param message
     * @param forceUpdate
     */
    protected void logTelemetry(String message, boolean forceUpdate) {
        if (DEBUG_MODE) {
            telemetry.addData(debugCaption, message);

            if (forceUpdate) {
                telemetry.update();
            }
        }
    }


    /**
     *
     */
    public void realtimeArm() {

        double armPower = gamepad.right_stick_y * armPowerMultiplier;

        if(gamepad.dpad_left) {
            ARM_ENCODER_LIMITS = false;
        } else {
            // when the limit switch is pressed, initialize the encoder limits
            // this is required just incase the arm does not reach the bottom position during init
            if(ARM_ENCODER_LIMITS == false) {
                this.ARM_ENCODER_BOTTOM = arm_drive.getCurrentPosition();
                this.ARM_ENCODER_TOP = arm_drive.getCurrentPosition() + ARM_ENCODER_SPAN;
                this.ARM_ENCODER_LIMITS = true;
            } else {
                // use bumpers to quick set top and bottom limits
                if (armPower == 0 && gamepad.right_bumper) {
                    arm_target = ARM_ENCODER_TOP;
                } else if (armPower == 0 && gamepad.left_bumper) {
                    arm_target = ARM_ENCODER_BOTTOM;
                } else if (armPower != 0) {
                    arm_target = 0;
                }
            }
        }





        // limit the power sent to the motor
        if (armPower < ARM_MAX_DOWN) {
            armPower = ARM_MAX_DOWN;
        } else if (armPower > ARM_MAX_UP) {
            armPower = ARM_MAX_UP;
        }

        // protect limits
        if(ARM_ENCODER_LIMITS) {
            if (arm_drive.getCurrentPosition() >= ARM_ENCODER_TOP && armPower > 0) {
                logTelemetry("TOP LIMIT!", true);
                armPower = 0;
            } else if (arm_drive.getCurrentPosition() <= ARM_ENCODER_BOTTOM && armPower < 0) {
                logTelemetry("BOTTOM LIMIT!", true);
                armPower = 0;
            }
        }
//        } else {
//            if (!arm_limit.isPressed() && armPower < 0) {
//                logTelemetry("BOTTOM LIMIT (SW)!", true);
//                armPower = 0;
//            }
//        }

        // move it!
        if(arm_target != 0) {
            arm_drive.setTargetPosition(arm_target);
        } else {
            arm_drive.setPower(armPower);
        }

        logTelemetry("arm power" + armPower + " arm target" + arm_target + " arm position: "+arm_drive.getCurrentPosition()+" limit sw:" + arm_limit.isPressed() + " use encoders:" + ARM_ENCODER_LIMITS);

        realtimeClaw();
    }

    /**
     *
     */
    private void realtimeClaw() {
        if (clawToggleReady && gamepad.b) {
            clawToggleReady = false;
            servoPosition = servoPosition * -1;
        } else if (!gamepad.b) {
            clawToggleReady = true;
        }

        if (servoPosition < MIN) {
            servoLocation = MIN;
        } else if (servoPosition > MAX) {
            servoLocation = MAX;
        } else {
            servoLocation = servoPosition;
        }

        claw_servo.setPosition(servoLocation);
        logTelemetry("toggle ready" + clawToggleReady + " location:" + servoLocation);
    }
}
