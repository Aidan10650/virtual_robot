package Calculators;

import Utilities.Vector2D;
import com.qualcomm.robotcore.hardware.DcMotor;

import Hardware.CompleteController;
import com.qualcomm.robotcore.hardware.DistanceSensor;

public class Interfaces {

    public static class MoveData{
        public static class Command{
            public double speed;
            public Vector2D motionSpeed;
            public double orientationSpeed;
            public Command(double speed, Vector2D motionSpeed, double orientationSpeed){
                this.speed = speed;
                this.motionSpeed = motionSpeed;
                this.orientationSpeed = orientationSpeed;
            }
        }

        public double heading = 0;
        public DistanceSensor frontDist;
        public boolean firstLoop = true;
        public double timeRemainingUntilEndgame = 0;
        public double timeRemainingUntilMatch = 0;
        public CompleteController driver, manip;
        public boolean isStarted, isFinished;
        public DcMotor bright, fright, bleft, fleft;
        public Command currentCommand = null;
        public Command lastCommand = null;
        public Vector2D wPos = new Vector2D(0,0);//current robot world position
        public Vector2D encoderPos = new Vector2D();
        public int currentSpin = 0;
        public boolean foundSpin = false;
        public double orientationError = 0;
        public double orientationP = 0.5;
        public double motionProgress = 0;
        public double orientationProgress = 0;
        public double speedProgress = 0;
        public double otherProgress = 0;
        public double previousSpeed = 0;
        public double progress;

    }

    public interface ProgressCalc{
        double myProgress(MoveData d);
    }


    public interface MotionCalc extends ProgressCalc{
        Vector2D CalcMotion(MoveData d);
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
