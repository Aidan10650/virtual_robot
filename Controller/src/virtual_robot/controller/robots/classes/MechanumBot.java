package virtual_robot.controller.robots.classes;

import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.ServoImpl;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.MotorType;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamNameImpl;
import sun.nio.fs.MacOSXFileSystemProvider;
import virtual_robot.controller.BotConfig;
import virtual_robot.controller.VirtualBot;
import virtual_robot.controller.VirtualRobotController;
import virtual_robot.util.AngleUtils;

/**
 * For internal use only. Represents a robot with four mechanum wheels, color sensor, four distance sensors,
 * a BNO055IMU, and a Servo-controlled arm on the back.
 *
 * MechanumBot is the controller class for the "mechanum_bot.fxml" markup file.
 *
 */
@BotConfig(name = "Mechanum Bot", filename = "mechanum_bot")
public class MechanumBot extends VirtualBot {

    MotorType motorType;
    private DcMotorImpl[] motors = null;
    //private VirtualRobotController.GyroSensorImpl gyro = null;
    private BNO055IMUImpl imu = null;
    private NavxMicroNavigationSensor navX = null;
    private VirtualRobotController.ColorSensorImpl colorSensor = null;
    private ServoImpl gripper, swinger, hooker, booker;
    private ModernRoboticsI2cRangeSensor[] distanceSensors = null;

    // backServoArm is instantiated during loading via a fx:id property.
    @FXML Rectangle backServoArm;

    private double wheelCircumference;
    private double interWheelWidth;
    private double interWheelLength;
    private double wlAverage;

    private double[][] tWR; //Transform from wheel motion to robot motion


    public MechanumBot(){
        super();
        motors = new DcMotorImpl[]{
                (DcMotorImpl)hardwareMap.dcMotor.get("bleft"),
                (DcMotorImpl)hardwareMap.dcMotor.get("fleft"),
                (DcMotorImpl)hardwareMap.dcMotor.get("fright"),
                (DcMotorImpl)hardwareMap.dcMotor.get("bright")
        };
        distanceSensors = new ModernRoboticsI2cRangeSensor[]{
                hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "frontRange"),
                hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "leftRange"),
                hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "backRange"),
                hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rightRange")
        };
        //gyro = (VirtualRobotController.GyroSensorImpl)hardwareMap.gyroSensor.get("gyro_sensor");
        imu = hardwareMap.get(BNO055IMUImpl.class, "imu");
        navX = hardwareMap.get(NavxMicroNavigationSensor.class, "navX");
        colorSensor = (VirtualRobotController.ColorSensorImpl)hardwareMap.colorSensor.get("color_sensor");

        gripper = (ServoImpl)hardwareMap.servo.get("firm grasp");
        swinger = (ServoImpl)hardwareMap.servo.get("ragtime");
        hooker = (ServoImpl)hardwareMap.servo.get("hooker");
        booker = (ServoImpl)hardwareMap.servo.get("booker");

        wheelCircumference = Math.PI * botWidth / 4.5;
        interWheelWidth = botWidth * 8.0 / 9.0;
        interWheelLength = botWidth * 7.0 / 9.0;
        wlAverage = (interWheelLength + interWheelWidth) / 2.0;

        tWR = new double[][] {
                {-0.25, 0.25, -0.25, 0.25},
                {0.25, 0.25, 0.25, 0.25},
                {-0.25/ wlAverage, -0.25/ wlAverage, 0.25/ wlAverage, 0.25/ wlAverage},
                {-0.25, 0.25, 0.25, -0.25}
        };
    }

    public void initialize(){
        //backServoArm = (Rectangle)displayGroup.getChildren().get(8);
        backServoArm.getTransforms().add(new Rotate(0, 37.5, 67.5));
    }

    protected void createHardwareMap(){
        motorType = MotorType.Neverest40;
        hardwareMap = new HardwareMap();
        String[] motorNames = new String[] {"bleft", "fleft", "fright", "bright"};
        for (String name: motorNames) hardwareMap.put(name, new DcMotorImpl(motorType));
        String[] distNames = new String[]{"frontRange", "leftRange", "backRange", "rightRange"};
        for (String name: distNames) hardwareMap.put(name, new ModernRoboticsI2cRangeSensor());
        //hardwareMap.put("gyro_sensor", controller.new GyroSensorImpl());
        hardwareMap.put("navX", new NavxMicroNavigationSensor(this,10));
        hardwareMap.put("imu", new BNO055IMUImpl(this, 10));
        hardwareMap.put("color_sensor", controller.new ColorSensorImpl());

        hardwareMap.put("firm grasp", new ServoImpl());
        hardwareMap.put("ragtime", new ServoImpl());
        hardwareMap.put("hooker", new ServoImpl());
        hardwareMap.put("booker", new ServoImpl());

        hardwareMap.put("stoned cam", new WebcamNameImpl());
    }

    public synchronized void updateStateAndSensors(double millis){

        double[] deltaPos = new double[4];
        double[] w = new double[4];

        for (int i = 0; i < 4; i++) {
            deltaPos[i] = motors[i].update(millis);
            w[i] = deltaPos[i] * wheelCircumference / motorType.TICKS_PER_ROTATION;
            if (i < 2) w[i] = -w[i];
        }

        double[] robotDeltaPos = new double[] {0,0,0,0};
        for (int i=0; i<4; i++){
            for (int j = 0; j<4; j++){
                robotDeltaPos[i] += tWR[i][j] * w[j];
            }
        }

        double dxR = robotDeltaPos[0];
        double dyR = robotDeltaPos[1];
        double headingChange = robotDeltaPos[2];
        double avgHeading = headingRadians + headingChange / 2.0;

        double sin = Math.sin(avgHeading);
        double cos = Math.cos(avgHeading);

        x += dxR * cos - dyR * sin;
        y += dxR * sin + dyR * cos;
        headingRadians += headingChange;

        if (x >  (halfFieldWidth - halfBotWidth)) x = halfFieldWidth - halfBotWidth;
        else if (x < (halfBotWidth - halfFieldWidth)) x = halfBotWidth - halfFieldWidth;
        if (y > (halfFieldWidth - halfBotWidth)) y = halfFieldWidth - halfBotWidth;
        else if (y < (halfBotWidth - halfFieldWidth)) y = halfBotWidth - halfFieldWidth;

        if (headingRadians > Math.PI) headingRadians -= 2.0 * Math.PI;
        else if (headingRadians < -Math.PI) headingRadians += 2.0 * Math.PI;

        imu.updateHeadingRadians(headingRadians);
        navX.updateHeadingRadians(headingRadians);

        colorSensor.updateColor(x, y);

        final double piOver2 = Math.PI / 2.0;

        for (int i = 0; i<4; i++){
            double sensorHeading = AngleUtils.normalizeRadians(headingRadians + i * piOver2);
            distanceSensors[i].updateDistance( x - halfBotWidth * Math.sin(sensorHeading),
                    y + halfBotWidth * Math.cos(sensorHeading), sensorHeading);
        }

    }

    public synchronized void updateDisplay(){
        super.updateDisplay();
        ((Rotate)backServoArm.getTransforms().get(0)).setAngle(-180.0 * hooker.getInternalPosition());
    }

    public void powerDownAndReset(){
        for (int i=0; i<4; i++) motors[i].stopAndReset();
        //gyro.deinit();
        imu.close();
    }


}
