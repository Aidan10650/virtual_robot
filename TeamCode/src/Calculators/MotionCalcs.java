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

//    public static Interfaces.MotionCalc PointMotion(double radius, MathUtil.Point... points) {
//
//        return new Interfaces.MotionCalc() {
//
//            @Override
//            public boolean doProgress(Interfaces.MoveData d) {
//                return false;
//            }
//            @Override
//            public MathUtil.Vector CalcMotion(Interfaces.MoveData d) {
//
//                if(d.forFirstLoop) {
//                    double preX = d.wX;
//                    double preY = d.wY;
//
//                    for (int i = 0; i < points.length; i++) {
//                        d.eX[i] = points[i].x;
//                        d.eY[i] = points[i].y;
//                    }
//
//                    for (int i = 0; i < points.length; i++) {
//                        d.line[i][0] = (d.eY[i] - preY) / (d.eX[i] - preX);
//                        d.line[i][1] = (d.eY[i] - ((d.line[i][0]) * d.eX[i]));
//                        preX = d.eX[i];
//                        preY = d.eY[i];
//                    }
//                    d.forFirstLoop = false;
//                }
//
//                d.lineNum += MathUtil.Distance(d.wX, d.wY, d.eX[d.lineNum], d.eY[d.lineNum]) < radius ? 1 : 0;
//
//                double h = d.wX;
//                double k = d.wY;
//                double m = d.line[d.lineNum][0];
//                double b = d.line[d.lineNum][1];
//                double r = radius;
//
//                MathUtil.Vector newDestination = new MathUtil.Vector();
//                MathUtil.Vector[] solves = {new MathUtil.Vector(), new MathUtil.Vector()};
//
//                solves[0].x = (h-m*b+m*k+Math.sqrt(-(Math.pow(m,2)*Math.pow(h,2))+(2*m*k*h)-(2*m*b*h)+(Math.pow(m,2)*Math.pow(r,2))+(2*b*k)+Math.pow(r,2)-Math.pow(b,2)-Math.pow(k,2)))/(1+Math.pow(m,2));
//                solves[1].x = (h-m*b+m*k-Math.sqrt(-(Math.pow(m,2)*Math.pow(h,2))+(2*m*k*h)-(2*m*b*h)+(Math.pow(m,2)*Math.pow(r,2))+(2*b*k)+Math.pow(r,2)-Math.pow(b,2)-Math.pow(k,2)))/(1+Math.pow(m,2));
//
//                //if two find the shortest distance from the solve to the endpoint
//                if(solves[0].x != NaN && solves[1].x != NaN) {
//                    solves[0].y = m*solves[0].x + b;
//                    solves[1].y = m*solves[1].x + b;
//                    double dist1 = MathUtil.Distance(solves[0].x,solves[0].y,d.eX[d.lineNum],d.eY[d.lineNum]);//create distance method to input points
//                    double dist2 = MathUtil.Distance(solves[1].x,solves[1].y,d.eX[d.lineNum],d.eY[d.lineNum]);
//                    if (dist2 > dist1){
//                        newDestination.x = solves[0].x;
//                        newDestination.y = solves[0].y;
//                    } else {
//                        newDestination.x = solves[1].x;
//                        newDestination.y = solves[1].y;
//                    }
//                } else if (solves[0].x == NaN && solves[1].x == NaN){
//                    newDestination.x = d.eX[d.lineNum];
//                    newDestination.y = d.eY[d.lineNum];
//                } else if (solves[0].x == NaN) {
//                    newDestination.x = solves[1].x;
//                    newDestination.y = solves[1].y;
//                } else if (solves[1].x == NaN) {
//                    newDestination.x = solves[0].x;
//                    newDestination.y = solves[0].y;
//                }
//                double deltaHeading = -(d.heading-(90-Math.toDegrees(Math.atan2(newDestination.y,newDestination.x))));
//
//                //MathUtil.Vector returnVal = new MathUtil.Vector(Math.cos(Math.toRadians(deltaHeading)),Math.sin(Math.toRadians(deltaHeading)));//the problem is here
//                double magVector = Math.hypot(newDestination.x - d.wX, newDestination.y - d.wX);
//                double magAdj = 10*(1/magVector);
//                MathUtil.Vector returnVal = new MathUtil.Vector((newDestination.x-d.wX)*magAdj,(newDestination.y-d.wY)*magAdj);
//                return returnVal;
//            }
//        };
//    }
    public static Interfaces.MotionCalc PointMotion(double radius, MathUtil.Point... points) {

        return new Interfaces.MotionCalc() {

            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
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
                        d.preX[i+1] = d.eX[i];
                        d.preY[i+1] = d.eY[i];
                    }
                    d.forFirstLoop = false;
                }

                if(d.lineNum < points.length-1) {
                    d.lineNum += MathUtil.Distance(d.wX, d.wY, d.eX[d.lineNum], d.eY[d.lineNum]) < radius ? 1 : 0;
                }
/*
//
//                VectorUtil currPos = new VectorUtil(d.wX, d.wY);
//                VectorUtil endPos = new VectorUtil(d.eX[d.lineNum],d.eY[d.lineNum]);
//                VectorUtil preEndPos = new VectorUtil(d.preX[d.lineNum],d.preY[d.lineNum]);
//
//
//                double perp = VectorUtil.PerpendicularDistance(currPos,endPos,preEndPos);
//                double hypot = radius;
//                double para = Math.sqrt(Math.pow(hypot,2)-Math.pow(perp,2));
//
//                MathUtil.Vector newDestination = new MathUtil.Vector(perp+d.wX,para+d.wY);
 */
                VectorUtil currPos = new VectorUtil(d.wX, d.wY);
                VectorUtil endPos = new VectorUtil(d.eX[d.lineNum],d.eY[d.lineNum]);
                VectorUtil preEndPos = new VectorUtil(d.preX[d.lineNum],d.preY[d.lineNum]);

                return new MathUtil.Vector(VectorUtil.LineHypotIntersect(currPos,endPos,preEndPos,radius));

//                double deltaHeading = -(d.heading - (90 - Math.toDegrees(Math.atan2(newDestination.y, newDestination.x))));

                //MathUtil.Vector returnVal = new MathUtil.Vector(Math.cos(Math.toRadians(deltaHeading)),Math.sin(Math.toRadians(deltaHeading)));//the problem is here
//                double magVector = Math.hypot(newDestination.x - d.wX, newDestination.y - d.wX);
//                double magAdj = (1 / magVector);
//                return new MathUtil.Vector((newDestination.x - d.wX) * magAdj, (newDestination.y - d.wY) * magAdj);
            }
        };
    }
}
