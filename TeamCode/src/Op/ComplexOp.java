package Op;

import Utilities.VectorUtil;
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

import static Utilities.MathUtil.Rotate2D;

public abstract class ComplexOp extends LinearOpMode{

    private MecanumDrive mecanumDrive = new MecanumDrive();
    private Robot robot = new Robot();

    float endGameTime = 0;

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
            double encoderPreY = d.encoderPos.y;
            double encoderPreX = d.encoderPos.x;
            d.encoderPos.y = mecanumDrive.getDistanceCm()[0];
            d.encoderPos.x = mecanumDrive.getDistanceCm()[1];
            Utilities.VectorUtil deltaMove = new Utilities.VectorUtil(d.encoderPos.x-encoderPreX,d.encoderPos.y-encoderPreY);
            deltaMove.rotateBy(Math.toRadians(d.heading));
            d.wX += deltaMove.x;
            d.wY += deltaMove.y;
            d.currentCommand.orientationSpeed = orientationCalc.CalcOrientation(d);
            d.currentCommand.motionSpeed = motionCalc.CalcMotion(d);
            Utilities.VectorUtil motion = new Utilities.VectorUtil(d.currentCommand.motionSpeed.x, d.currentCommand.motionSpeed.y);
            motion.rotateBy(Math.toRadians(-d.heading));
            d.currentCommand.motionSpeed.x = motion.x;
            d.currentCommand.motionSpeed.y = motion.y;
            d.currentCommand.speed = speedCalc.CalcSpeed(d);

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

            double distances[] = mecanumDrive.getDistanceCm();
            if (d.timeRemainingUntilEndgame >= 0) endGameTime = (float)(Math.round(d.timeRemainingUntilEndgame / 100) / 10.0);

            telemetry.addData("Progress", Math.round(d.progress*1000)/10.0);
            telemetry.addData("time until endgame", endGameTime);
            telemetry.addData("time until end of match",Math.round(d.timeRemainingUntilMatch/100)/10.0);
//            telemetry.addData("distance fwd", distances[0]);
            telemetry.addData("my distance fwd", Math.round(d.wY*10)/10.0);
//            telemetry.addData("distance right", distances[1]);
            telemetry.addData("my distance side", Math.round(d.wX*10)/10.0);
            telemetry.addData("heading", Math.round(d.heading*10)/10.0);
            telemetry.addData("orientation", Math.round(d.currentCommand.orientationSpeed*10)/10.0);
            telemetry.addData("motiony", Math.round(d.currentCommand.motionSpeed.y*10)/10.0);
            telemetry.addData("motionx", Math.round(d.currentCommand.motionSpeed.x*10)/10.0);
            telemetry.addData("speed",Math.round(d.currentCommand.speed*10)/10.0);
            telemetry.update();

            mecanumDrive.driveMecanum(d.currentCommand.motionSpeed.y*d.currentCommand.speed,
                    d.currentCommand.motionSpeed.x*d.currentCommand.speed,
                    d.currentCommand.orientationSpeed);
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
