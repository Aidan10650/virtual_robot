package Calculators;


import com.qualcomm.robotcore.hardware.GamePad;
import Control.TeleControl.CompleteController;
import Control.TeleControl.FieldBasedControl;
import Hardware.Box.RobotMap;
//import Hardware.NavX;
import Hardware.ReadPosition;
import Utilities.MathUtil;
import org.firstinspires.ftc.teamcode.ftc16072.Navigation;

public class MotionCalcs { //This will always output a power on the x axis of the robot and a power on the y axis
    CompleteController completeDriver;
//    private NavX gyro;
    double wX, wY, radius;// world X and Y & end point X and Y
    double[] eX, eY;
    double[][] line;
    int lineNum = 0;

    public void enable(){
       // gyro = RobotMap.gyro;

    }
    public static Interfaces.MotionCalc moveWithFieldCentricJoystick() {
        return new Interfaces.MotionCalc() {
            @Override
            public MathUtil.Vector CalcMotion(Interfaces.MoveData d) {
                MathUtil.Vector power = (d.driver.getLeftStick());
                return FieldBasedControl.getJoystick(power, d.heading);
            }

            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }
        };
    }

    public static Interfaces.MotionCalc moveWithObjectCentricJoystick(){
        return new Interfaces.MotionCalc() {
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public MathUtil.Vector CalcMotion(Interfaces.MoveData d) {
                MathUtil.Vector power = d.driver.getLeftStick();
                return power;
            }
        };
    }

    public void MotionCalcs(double radius, MathUtil.Point... points) {
        this.wX = ReadPosition.getPosition().x;
        this.wY = ReadPosition.getPosition().y;
        double preX = this.wX;
        double preY = this.wY;
        this.radius = radius;
        for (int i = 0; i <= points.length; i++) {
            this.eX[i] = points[i].x;
            this.eY[i] = points[i].y;
        }
        for (int i = 0; i <= eX.length; i++){
            this.line[i][0] = (eY[i] - preY) / (eX[i] - preX);
            this.line[i][1] = (eY[i]/((line[i][0])*eX[i]));
            preX = eY[i];
            preY = eX[i];
        }
        lineNum += MathUtil.Distance(wX,wY,eX[lineNum],eY[lineNum]) <= radius ? 1 : 0;



    }
}
