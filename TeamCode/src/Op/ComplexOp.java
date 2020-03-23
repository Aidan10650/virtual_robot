package Op;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import Calculators.Interfaces;
import Calculators.OrientationCalcs;
import Control.TeleControl.CompleteController;
import Utilities.MathUtil;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ftc16072.MecanumDrive;
import org.firstinspires.ftc.teamcode.ftc16072.Robot;

public abstract class ComplexOp extends LinearOpMode{

    private MecanumDrive mecanumDrive = new MecanumDrive();
    private Robot robot = new Robot();


    public void ComplexMove(Interfaces.SpeedCalc speedCalc,
                            Interfaces.MotionCalc motionCalc,
                            Interfaces.OrientationCalc orientationCalc,
                            Interfaces.OtherCalc... otherCalc) throws InterruptedException{


        mecanumDrive.init(hardwareMap);

        d.progress = 0;
        MathUtil.Vector vector = new MathUtil.Vector();
        d.lastCommand = d.currentCommand;
        d.currentCommand = new Interfaces.MoveData.Command(0, vector,0.0);


        while(d.progress <= 1) {
            d.heading = robot.nav.getHeading(AngleUnit.DEGREES);
            d.currentCommand.speed = speedCalc.CalcSpeed(d);
            d.currentCommand.motionSpeed = motionCalc.CalcMotion(d);
            d.currentCommand.orientationSpeed = orientationCalc.CalcOrientation(d);
            for (Interfaces.OtherCalc calc : otherCalc) {
                calc.CalcOther(d);
            }
            boolean didOtherProgress = false;
            for (Interfaces.OtherCalc calc : otherCalc) {
                didOtherProgress = calc.doProgress(d);
                if (didOtherProgress) break;
            }
            boolean didProgress =
                    didOtherProgress ||
                    speedCalc.doProgress(d) ||
                    motionCalc.doProgress(d) ||
                    orientationCalc.doProgress(d);

//            d.currentCommand.motionSpeed.x *= d.currentCommand.speed;
//            d.currentCommand.motionSpeed.y *= d.currentCommand.speed;
//            d.currentCommand.orientationSpeed *= d.currentCommand.speed;


            double distances[] = mecanumDrive.getDistanceCm();
            telemetry.addData("time until endgame",d.timeRemainingUntilEndgame/1000);
            telemetry.addData("time until end of match",d.timeRemainingUntilMatch/1000);
            telemetry.addData("distance fwd", distances[0]);
            telemetry.addData("distance right", distances[1]);
            telemetry.addData("orientation", d.currentCommand.orientationSpeed);
            telemetry.addData("motiony", d.currentCommand.motionSpeed.y);
            telemetry.addData("motionx", d.currentCommand.motionSpeed.x);
            telemetry.addData("speed",d.currentCommand.speed);
            telemetry.update();

            mecanumDrive.driveMecanum(d.currentCommand.motionSpeed.y*d.currentCommand.speed,
                    d.currentCommand.motionSpeed.x*d.currentCommand.speed,
                    d.currentCommand.orientationSpeed*d.currentCommand.speed);
//            d.frightPow = +d.currentCommand.motionSpeed.x + d.currentCommand.motionSpeed.y + d.currentCommand.orientationSpeed;
//            d.brightPow = -d.currentCommand.motionSpeed.x + d.currentCommand.motionSpeed.y + d.currentCommand.orientationSpeed;
//            d.bleftPow = +d.currentCommand.motionSpeed.x + d.currentCommand.motionSpeed.y - d.currentCommand.orientationSpeed;
//            d.fleftPow = -d.currentCommand.motionSpeed.x + d.currentCommand.motionSpeed.y - d.currentCommand.orientationSpeed;
//
//            double adjust = MathUtil.ScaleAdjustment(1,d.frightPow,d.brightPow,d.bleftPow,d.fleftPow);
//
//            d.bright.setPower(adjust*d.brightPow);
//            d.fright.setPower(adjust*d.frightPow);
//            d.bleft.setPower(adjust*d.bleftPow);
//            d.fleft.setPower(adjust*d.fleftPow);

            if (!opModeIsActive()) {
                throw new InterruptedException();
            }
        }
    }

    private Interfaces.MoveData d = new Interfaces.MoveData();

    void init(HardwareMap hwMap) {
        robot.init(hwMap);
        d.fleft = hwMap.get(DcMotor.class, "front_left_motor");
        d.fleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        d.fright = hwMap.get(DcMotor.class, "front_right_motor");
        d.fright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        d.bleft = hwMap.get(DcMotor.class, "back_left_motor");
        d.bleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        d.bright = hwMap.get(DcMotor.class, "back_right_motor");
        d.bright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        d.bleft.setDirection(DcMotor.Direction.REVERSE);
        d.fleft.setDirection(DcMotor.Direction.REVERSE);
    }

    public void initHardware() {

//        RobotMap map = new RobotMap(hardwareMap);
//
//        d.bright = map.bright;
//        d.fright = map.fright;
//        d.bleft = map.bleft;
//        d.fleft = map.fleft;
    }

    public abstract void body() throws InterruptedException;

    public void exit(){
        d.bright.setPower(0);
        d.fright.setPower(0);
        d.bleft.setPower(0);
        d.fleft.setPower(0);
    }



    @Override
    public void runOpMode() throws InterruptedException{

        //INITIALIZATION
        telemetry.addData("Initializing", "Started");
        telemetry.update();

        d.isFinished = false;
        d.isStarted = false;

        d.driver = new CompleteController();
        d.manip = new CompleteController();

        d.driver.CompleteController(gamepad1);
        d.manip.CompleteController(gamepad2);

        init(hardwareMap);

        initHardware();

        telemetry.addData("Initializing", "Finished");
        telemetry.update();

        waitForStart();

        d.isStarted = true;

        telemetry.addData("First Loop", "Finished");
        telemetry.update();


        //BODY

        try {
            body();
        } catch (InterruptedException ie) { }

        //EXIT
        telemetry.addData("Exit", "Started");
        telemetry.update();
        exit();
        d.isFinished = true;
    }
}
