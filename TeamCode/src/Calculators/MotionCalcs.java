package Calculators;


import Utilities.Vector2D;
import Utilities.*;
import org.firstinspires.ftc.teamcode.ftc16072.MecanumDrive;

import java.util.ArrayList;

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
            public Vector2D CalcMotion(Interfaces.MoveData d) {
                return d.driver.getLeftStick();
            }
        };
    }

    public static Interfaces.MotionCalc PointMotion(double radius, Vector2D... points) {

        return new Interfaces.MotionCalc() {

            private boolean firstLoop = true;
            private double totalDist = 0;
            private double worldDist = 0;
            private ArrayList<Vector2D> ePosArray = new ArrayList<Vector2D>();
            private ArrayList<Vector2D> preEPosArray = new ArrayList<Vector2D>();
//            private double[] eX = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};//I might fix this at some point, but you cannot create
//            private double[] eY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};// a new value on an array so I am not making a new value
//            private double[] preX = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};// I am editing a zero
//            private double[] preY = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};// Could use an ArrayList but this does the same thing and ArrayLists are painful
            private int lineNum = 0;
            private Vector2D preWPos = new Vector2D(0,0);

            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return true;
            }

            @Override
            public Vector2D CalcMotion(Interfaces.MoveData d) {

                //to initialize all of the end points
                if (firstLoop) {
                    //setting the first point to where the robot starts || this could also be a chosen starting position
                    preEPosArray.add(0, d.wPos.clone());

                    for (int i = 0; i < points.length; i++) {
                        //copying all of the arguments from PointMotion into a this class// because I can
                        ePosArray.add(i, points[i].clone());
                    }

                    for (int i = 0; i < points.length; i++) {
                        //adds up the distance of all of the line segments
                        totalDist += ePosArray.get(i).distance(preEPosArray.get(i));
                        //setting the previous endpoint
                        // i+1 pre instead of i-1 e avoids having to deal with null pointer exceptions
                        preEPosArray.add(i+1,ePosArray.get(i).clone());
                    }
                    //so it doesn't loop again //very important
                    firstLoop = false;
                }


                 //Determining if to go to the next line
                if(lineNum < points.length-1) lineNum += d.wPos.distance(ePosArray.get(lineNum)) < radius ? 1 : 0;

                //this is set to the current world position
                Vector2D currPos = new Vector2D(d.wPos.x, d.wPos.y);
                //setting a vector to the next end point
                Vector2D endPos = new Vector2D(ePosArray.get(lineNum).x,ePosArray.get(lineNum).y);
                //setting a vector to the previous end point //on the first line this is set to the current world position
                Vector2D preEndPos = new Vector2D(preEPosArray.get(lineNum).x,preEPosArray.get(lineNum).y);

                //Adding to the total distance traveled
                worldDist += d.wPos.distance(preWPos);

                //Making a ratio of how much we have traveled over what we should travel to create progress
                d.progress = worldDist/totalDist;

                //setting this for the next loop
                preWPos.set(d.wPos);

                if(lineNum == points.length) d.progress = 1.0;

                return new Vector2D(Vector2D.LineHypotIntersect(currPos,endPos,preEndPos,radius));

            }
        };
    }
}
