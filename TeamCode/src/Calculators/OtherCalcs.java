package Calculators;

import Utilities.MathUtil;
import Utilities.TimeUtil;

public class OtherCalcs {
    public static Interfaces.OtherCalc whileOpMode(){

        return new Interfaces.OtherCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return true;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                d.progress = 0.5;
            }
        };
    }
    public static Interfaces.OtherCalc TeleOpMatch(){

        Utilities.TimeUtil matchTime = new Utilities.TimeUtil();
        Utilities.TimeUtil endGameTime = new Utilities.TimeUtil();

        return new Interfaces.OtherCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return true;
            }

            @Override
            public void CalcOther(Interfaces.MoveData d){
                if(d.firstLoop){
                  endGameTime.startTimer(120000);
                  matchTime.startTimer(150000);
                  d.firstLoop=false;
                }

                d.timeRemainingUntilEndgame = endGameTime.timeRemaining();
                d.timeRemainingUntilMatch = matchTime.timeRemaining();
                d.progress = 1-(d.timeRemainingUntilMatch/150000);

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
//            public boolean doProgress(Interfaces.MoveData d) {
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
