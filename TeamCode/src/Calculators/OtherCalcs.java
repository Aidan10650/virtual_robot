package Calculators;

import Utilities.MathUtil;
import Utilities.TimeUtil;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class OtherCalcs {
    public static Interfaces.OtherCalc whileOpMode(){

        return new Interfaces.OtherCalc(){
            double myProgress;
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                myProgress = 0.5;
            }
        };
    }


    public static Interfaces.OtherCalc TeleOpMatch(){

        Utilities.TimeUtil matchTime = new Utilities.TimeUtil();
        Utilities.TimeUtil endGameTime = new Utilities.TimeUtil();

        return new Interfaces.OtherCalc(){
            private double myProgress;
            private boolean firstLoop = true;
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                if(firstLoop){
                  endGameTime.startTimer(120000);
                  matchTime.startTimer(150000);
                  firstLoop=false;
                }

                d.timeRemainingUntilEndgame = endGameTime.timeRemaining();
                d.timeRemainingUntilMatch = matchTime.timeRemaining();
                myProgress = 1-(d.timeRemainingUntilMatch/150000);

            }
        };
    }
    public static Interfaces.OtherCalc DistanceStop(double startStopDist, double stopStopDist, double startProgress, double endProgress){

        return new Interfaces.OtherCalc(){
            private double myProgress = 0;
            @Override
            public double myProgress(Interfaces.MoveData d) {
                return myProgress;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                if(startStopDist > d.frontDist.getDistance(DistanceUnit.CM)){
                    myProgress = startProgress+(endProgress - startProgress)*((startStopDist-d.frontDist.getDistance(DistanceUnit.CM))
                                /(startStopDist-stopStopDist));
                } else if (stopStopDist > d.frontDist.getDistance(DistanceUnit.CM)){
                    myProgress = 1.0;
                }
            }
        };
    }
//    public static Interfaces.OtherCalc TimeProgress(){
//
//        TimeUtil matchTime = new TimeUtil();
//        TimeUtil endGameTime = new TimeUtil();
//
//        return new Interfaces.OtherCalc(){
//            @Override
//            public double myProgress(Interfaces.MoveData d) {
//                return true;
//            }
//
//            @Override
//            public void CalcOther(Interfaces.MoveData d){
//                if(d.firstLoop){
//                    endGameTime.startTimer(120000);
//                    matchTime.startTimer(150000);
//                    d.firstLoop=false;
//                }
//
//                d.timeRemainingUntilEndgame = endGameTime.timeRemaining();
//                d.timeRemainingUntilMatch = matchTime.timeRemaining();
//                d.progress = 1-(d.timeRemainingUntilMatch/150000);
//
//            }
//        };
//    }
}
