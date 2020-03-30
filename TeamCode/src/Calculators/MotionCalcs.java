package Calculators;


import Utilities.VectorUtil;
import com.qualcomm.robotcore.hardware.GamePad;
import Control.TeleControl.CompleteController;
import Control.TeleControl.FieldBasedControl;
import Hardware.Box.RobotMap;
//import Hardware.NavX;
import Hardware.ReadPosition;
import Utilities.MathUtil;
import org.firstinspires.ftc.teamcode.ftc16072.MecanumDrive;
import org.firstinspires.ftc.teamcode.ftc16072.Navigation;

import static java.lang.Double.NaN;

public class MotionCalcs { //This will always output a power on the x axis of the robot and a power on the y axis

    private MecanumDrive mecanumDrive = new MecanumDrive();

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

    public static Interfaces.MotionCalc PointMotion(double radius, MathUtil.Point... points) {

        return new Interfaces.MotionCalc() {

            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return true;
            }

            @Override
            public MathUtil.Vector CalcMotion(Interfaces.MoveData d) {

                if (d.forFirstLoop) {
                    d.preX[0] = d.wX;
                    d.preY[0] = d.wY;

                    for (int i = 0; i < points.length; i++) {
                        d.eX[i] = points[i].x;
                        d.eY[i] = points[i].y;
                    }

                    for (int i = 0; i < points.length; i++) {
                        d.line[i][0] = (d.eY[i] - d.preY[i]) / (d.eX[i] - d.preX[i]);
                        d.line[i][1] = (d.eY[i] - ((d.line[i][0]) * d.eX[i]));
                        d.totalDist += MathUtil.Distance(d.eX[i],d.eY[i],d.preX[i],d.preY[i]);
                        d.preX[i+1] = d.eX[i];
                        d.preY[i+1] = d.eY[i];
                    }
                    d.forFirstLoop = false;
                }

                if(d.lineNum < points.length-1) d.lineNum += MathUtil.Distance(d.wX, d.wY, d.eX[d.lineNum], d.eY[d.lineNum]) < radius ? 1 : 0;

                VectorUtil currPos = new VectorUtil(d.wX, d.wY);
                VectorUtil endPos = new VectorUtil(d.eX[d.lineNum],d.eY[d.lineNum]);
                VectorUtil preEndPos = new VectorUtil(d.preX[d.lineNum],d.preY[d.lineNum]);

                d.worldDist += MathUtil.Distance(d.wX,d.wY,d.preWX,d.preWY);

                d.progress = d.worldDist/d.totalDist;

                d.preWX = d.wX;
                d.preWY = d.wY;

                return new MathUtil.Vector(VectorUtil.LineHypotIntersect(currPos,endPos,preEndPos,radius));

            }
        };
    }
}
