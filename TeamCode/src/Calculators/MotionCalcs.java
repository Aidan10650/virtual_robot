package Calculators;


import Utilities.Vector2D;
import Utilities.*;
import org.firstinspires.ftc.teamcode.ftc16072.MecanumDrive;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Vector;

public class MotionCalcs { //This will always output a power on the x axis of the robot and a power on the y axis

    private MecanumDrive mecanumDrive = new MecanumDrive();

    public static Interfaces.MotionCalc moveWithFieldCentricJoystick() {
        return new Interfaces.MotionCalc() {
            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                Vector2D power = (d.driver.getLeftStick());
                return power.getRotatedBy(-d.heading);
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }
        };
    }

    public static Interfaces.MotionCalc moveWithObjectCentricJoystick(){
        return new Interfaces.MotionCalc() {
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return 0;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                return d.driver.getLeftStick();
            }
        };
    }

    public static Interfaces.MotionCalc PointMotion(double turnRadius, Vector2D... points) {


        return new Interfaces.MotionCalc() {

            private double myProgress = 0;
            private boolean firstLoop = true;
            private double totalDist = 0;
            private double worldDist = 0;
            private ArrayList<Vector2D> ePosArray = new ArrayList<Vector2D>();
            private ArrayList<Vector2D> preEPosArray = new ArrayList<Vector2D>();
            private ArrayList<CurveData> curveDataArray = new ArrayList<CurveData>();
            private int lineNum = 0;
            private Vector2D preWPos = new Vector2D(0,0);
            private boolean curve;

            class CurveData{
                double curveLength;
                double arcAngle;
                double arcSegCoverDist;
                double radius = turnRadius;
                CurveData (double arcAngle){
                    this.arcAngle = arcAngle;
                    this.arcSegCoverDist = Math.tan(Math.toRadians(this.arcAngle/2))*radius;
                    this.curveLength = Math.PI*2.0*radius*this.arcAngle/360.0;
                }
            }



            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {

                try {
                    //to initialize all of the end points
                    if (firstLoop) {
                        //setting the first point to where the robot starts || this could also be a chosen starting position
                        preEPosArray.add(0, d.wPos.clone());

                        for (int i = 0; i < points.length; i++) {
                            //copying all of the arguments from PointMotion into a this class// because I can
                            ePosArray.add(i, points[i].clone());
//                        //adds up the distance of all of the line segments
                            totalDist += ePosArray.get(i).distance(preEPosArray.get(i));
                            //setting the previous endpoint
                            // i+1 pre instead of i-1 e avoids having to deal with null pointer exceptions
                            preEPosArray.add(i + 1, ePosArray.get(i).clone());

                        }

                        for (int i = 0; i < points.length - 1; i++) {
                            Vector2D preVec = new Vector2D(preEPosArray.get(i).x - ePosArray.get(i).x,
                                    preEPosArray.get(i).y - ePosArray.get(i).y);
                            Vector2D postVec = new Vector2D(ePosArray.get(i + 1).x - ePosArray.get(i).x,
                                    ePosArray.get(i + 1).y - ePosArray.get(i).y);
                            double segAngDiff = Vector2D.angleDifferenceDeg(preVec, postVec);
                            double arcAngDiff = 180 - segAngDiff;
                            curveDataArray.add(i, new CurveData(arcAngDiff));
                            totalDist += curveDataArray.get(i).curveLength - curveDataArray.get(i).arcSegCoverDist * 2;
                        }
                        //so it doesn't loop again //very important
                        firstLoop = false;
                    }

                    boolean found = false;
                    double previousDistances = 0;
                    for (int i = 0; !found && i < curveDataArray.size(); i++) {
                        if (worldDist < previousDistances + ePosArray.get(i).distance(preEPosArray.get(i)) - curveDataArray.get(i).arcSegCoverDist + curveDataArray.get(i).curveLength) {
                            lineNum = i;
                            found = true;
                        }
                        previousDistances += ePosArray.get(i).distance(preEPosArray.get(i));
                    }
                }
                catch(java.lang.IndexOutOfBoundsException e)
                {
                    System.out.println("Got exception");
                    return new Vector2D();
                }

                    if (       worldDist < ePosArray.get(lineNum).distance(preEPosArray.get(lineNum)) + curveDataArray.get(lineNum).arcSegCoverDist
                            && worldDist > ePosArray.get(lineNum).distance(preEPosArray.get(lineNum)) - curveDataArray.get(lineNum).arcSegCoverDist && lineNum != ePosArray.size()) {
                        curve = true;
                    } else {
                        curve = false;
                    }

//                if(worldDist > ePosArray.get(lineNum).distance(preEPosArray.get(lineNum))-curveDataArray.get(lineNum).arcSegCoverDist){
//                    if(lineNum != ePosArray.size()) {
//                        curve = true;
//                    } else {
//                        curve = false;
//                    }
//                } else if (worldDist < ePosArray.get(lineNum).distance(preEPosArray.get(lineNum))+curveDataArray.get(lineNum).arcSegCoverDist){
//                    if(lineNum != 0) {
//                        curve = true;
//                    } else {
//                        curve = false;
//                    }
//                } else {
//                    curve = false;
//                }

                    //Determining if to go to the next line
//                if(lineNum < points.length-1) lineNum += d.wPos.distance(ePosArray.get(lineNum)) < turnRadius ? 1 : 0;
                    //this is set to the current world position
                    Vector2D currPos = new Vector2D(d.wPos.x, d.wPos.y);
                    Vector2D endPos = new Vector2D();
                    Vector2D preEndPos = new Vector2D();

                    //Making a ratio of how much we have traveled over what we should travel to create progress
                    myProgress = worldDist / totalDist;

                    System.out.print("ePosArray.size: ");
                    System.out.println(ePosArray.size()); // TODO: Delete
                    System.out.print("preEPosArray.size: ");
                    System.out.println(preEPosArray.size()); // TODO: Delete
                    System.out.print("curveDataArray.size: ");
                    System.out.println(curveDataArray.size()); // TODO: Delete
                    System.out.print("lineNum: ");
                    System.out.println(lineNum); // TODO: Delete
                    System.out.print("worldDist: ");
                    System.out.println(worldDist); // TODO: Delete
                    System.out.print("totalDist: ");
                    System.out.println(totalDist); // TODO: Delete

                    //setting a vector to the next end point
                    endPos.set(ePosArray.get(lineNum).x, ePosArray.get(lineNum).y);
                    //setting a vector to the previous end point //on the first line this is set to the current world position
                    preEndPos.set(preEPosArray.get(lineNum).x, preEPosArray.get(lineNum).y);

                    if (lineNum == points.length) d.progress = 1.0;

                    //Adding to the total distance traveled
                    worldDist += d.wPos.distance(preWPos);
                    //setting this for the next loop
                    preWPos.set(d.wPos.clone());

                    if (curve) {
                        d.debugData1 = lineNum;
                        double distanceSegments = 0;
                        double distanceCurves = 0;
                        for (int i = 0; i < lineNum + 1; i++) {
                            distanceSegments += preEPosArray.get(i).distance(ePosArray.get(i));
                        }
                        distanceSegments -= 2 * lineNum * curveDataArray.get(lineNum).arcSegCoverDist + curveDataArray.get(lineNum).arcSegCoverDist;

                        for (int i = 0; i < lineNum; i++) {
                            distanceCurves += curveDataArray.get(i).curveLength;
                        }

                        double progress = (worldDist - (distanceCurves + distanceSegments)) / curveDataArray.get(lineNum).curveLength;
                        d.debugData2 = progress;
                        Vector2D startVec = new Vector2D(endPos.getSubtracted(preEndPos));
                        return startVec.getRotatedBy(progress * Math.toRadians(curveDataArray.get(lineNum).arcAngle)).getNormalized();
                    } else {
                        return new Vector2D(Vector2D.LineHypotIntersect(currPos, endPos, preEndPos, turnRadius));
                    }
            }
        };
    }
}
