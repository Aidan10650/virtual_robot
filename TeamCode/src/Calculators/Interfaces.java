package Calculators;

import com.qualcomm.robotcore.hardware.DcMotor;

import Control.TeleControl.CompleteController;
import Utilities.MathUtil;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.ftc16072.Navigation;

import java.util.ArrayList;

public class Interfaces {



    public static class MoveData{
        public static class Command{
            public double speed;
            public MathUtil.Vector motionSpeed;
            public double orientationSpeed;
            public Command(double speed, MathUtil.Vector motionSpeed, double orientationSpeed){
                this.speed = speed;
                this.motionSpeed = motionSpeed;
                this.orientationSpeed = orientationSpeed;
            }
        }
        public double heading = 0;
        public boolean firstLoop = true;
        public double timeRemainingUntilEndgame = 0;
        public double timeRemainingUntilMatch = 0;
        public CompleteController driver, manip;
        public boolean isStarted, isFinished;
        public DcMotor bright, fright, bleft, fleft;
        public Command currentCommand = null;
        public Command lastCommand = null;
        public double wX;//current robot world position
        public double wY;
        public Utilities.VectorUtil encoderPos = new Utilities.VectorUtil();
        public double[] eX = {0,0,0,0,0,0,0,0,0,0,0};
        public double[] eY = {0,0,0,0,0,0,0,0,0,0,0};
        public double[] preX = {0,0,0,0,0,0,0,0,0,0,0};
        public double[] preY = {0,0,0,0,0,0,0,0,0,0,0};
        public double[][] line = {{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},{0,0},};
        public int lineNum = 0;
        public boolean forFirstLoop = true;
        public boolean testFirstLoop = true;
        public double progress;

    }

    public interface ProgressCalc{
        boolean doProgress(MoveData d);
    }


    public interface MotionCalc extends ProgressCalc{
        MathUtil.Vector CalcMotion(MoveData d);
    }

    public interface OrientationCalc extends ProgressCalc{
        double CalcOrientation(MoveData d);
    }

    public interface SpeedCalc extends ProgressCalc{
        double CalcSpeed(MoveData d);
    }

    public interface OtherCalc extends ProgressCalc{
        void CalcOther(MoveData d);
    }
}
