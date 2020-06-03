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
            private ArrayList<SegmentData> segmentDataArray = new ArrayList<SegmentData>();
            private Vector2D preWPos = new Vector2D(0,0);

            // Sets world dist information for all segments and returns the total
            double CalcWordDists()
            {
                double total = 0;
                double lastDist = 0;
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    total += segmentDataArray.get(i).length;
                    segmentDataArray.get(i).worldStartDist = lastDist;
                    segmentDataArray.get(i).worldEndDist = total;
                    lastDist = total;
                }
                return total;
            }
            SegmentData GetSegmentByWorldDist(double worldDist){
                for (int i=0; i < segmentDataArray.size(); i++)
                {
                    SegmentData candidate = segmentDataArray.get(i);

                    if (worldDist >= candidate.worldStartDist &&
                        worldDist <= candidate.worldEndDist)
                    {
                        return segmentDataArray.get(i);
                    }
                }
                // Return the last one if none are in range
                return segmentDataArray.get(segmentDataArray.size()-1);
            }


            class SegmentData{
                double worldStartDist;
                double worldEndDist;
                double length;
                Vector2D startDirection;
                Vector2D startPos;
                Vector2D endPos;
            }
            class StraightData extends SegmentData {
                StraightData(double length){
                    this.length = length;
                }
            }
            class CurveData extends SegmentData{
                double curveLength;
                double arcAngle;
                double arcSegCoverDist;
                double radius = turnRadius;
                CurveData (double arcAngle){
                    this.arcAngle = arcAngle;
                    this.arcSegCoverDist = Math.tan(Math.toRadians(this.arcAngle/2))*radius;
                    this.curveLength = Math.abs(Math.PI*2.0*radius*this.arcAngle/360.0);
                    this.length = curveLength;
                }
            }

            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                //to initialize all of the end points
                if (firstLoop) {
                    //setting the first point to where the robot starts || this could also be a chosen starting position
                    preEPosArray.add(0, d.wPos.clone());

                    CurveData lastCurve = null;
                    for (int i = 0; i < points.length; i++) {
                        //copying all of the arguments from PointMotion into a this class// because I can
                        ePosArray.add(i, points[i].clone());
                        //setting the previous endpoint
                        // i+1 pre instead of i-1 e avoids having to deal with null pointer exceptions
                        preEPosArray.add(i + 1, ePosArray.get(i).clone());
                    }
                    for (int i = 0; i < points.length; i++) {
                        double fullSegmentLength = ePosArray.get(i).distance(preEPosArray.get(i));

                        SegmentData tempStraight = new StraightData(fullSegmentLength);
                        if (lastCurve != null)
                        {
                            // Remove the first portion of the straight due to the curve
                            tempStraight.length -= lastCurve.arcSegCoverDist;
                        }
                        tempStraight.startDirection = ePosArray.get(i).getSubtracted(preEPosArray.get(i)).getNormalized();
                        tempStraight.startPos = preEPosArray.get(i).clone();
                        tempStraight.endPos = ePosArray.get(i).clone(); // To be replaced if there is a curve
                        segmentDataArray.add(tempStraight);

                        // Add the curve info
                        if (i < points.length - 1) {
                            Vector2D preVec = new Vector2D(ePosArray.get(i).x - preEPosArray.get(i).x,
                                    ePosArray.get(i).y - preEPosArray.get(i).y);
                            Vector2D postVec = new Vector2D(ePosArray.get(i + 1).x - ePosArray.get(i).x,
                                    ePosArray.get(i + 1).y - ePosArray.get(i).y);
                            double arcAngDiff = Vector2D.angleDifferenceDeg(postVec, preVec);
                            CurveData tempCurve = new CurveData(arcAngDiff);
                            tempCurve.startDirection = tempStraight.startDirection.clone();
                            tempCurve.startPos = ePosArray.get(i).getSubtracted(tempCurve.startDirection.getMultiplied(tempCurve.arcSegCoverDist));
                            tempCurve.endPos = tempCurve.startPos.getRotatedBy(Math.toRadians(tempCurve.arcAngle));
                            segmentDataArray.add(tempCurve);
                            lastCurve = tempCurve;
                            tempStraight.length -= tempCurve.arcSegCoverDist;
                            tempStraight.endPos = tempCurve.startPos.clone();
                        }
                    }
                    totalDist = CalcWordDists();
                    //so it doesn't loop again //very important
                    firstLoop = false;
                }

                worldDist += d.wPos.distance(preWPos);
                //setting this for the next loop
                preWPos.set(d.wPos);
                //Making a ratio of how much we have traveled over what we should travel to create progress
                myProgress = worldDist / totalDist;

                SegmentData currentSegment = GetSegmentByWorldDist(worldDist);
                double segmentProgress = (worldDist - currentSegment.worldStartDist)/currentSegment.length;
                //System.out.print("segmentProgress: ");System.out.println(segmentProgress);
                Vector2D rval = null;
                if (currentSegment instanceof CurveData)
                {
                    CurveData curveSegment = (CurveData) currentSegment;
                    rval = currentSegment.startDirection.getRotatedBy(segmentProgress * Math.toRadians(curveSegment.arcAngle));
                }
                else
                {
                    rval = currentSegment.endPos.getSubtracted(d.wPos).getNormalized();
                    //Alternative that doesn't use current location: rval = currentSegment.startDirection;
                }
                return rval;
            }
        };
    }
}
