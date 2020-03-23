package Calculators;

import Utilities.MathUtil;

public class SpeedCalcs {
    public static Interfaces.SpeedCalc topSpeed(double desiredSpeed){

        return new Interfaces.SpeedCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public double CalcSpeed(Interfaces.MoveData d){
                return MathUtil.CompleteScaleAdjust(desiredSpeed, d.currentCommand.motionSpeed.x,d.currentCommand.motionSpeed.y);
            }
        };
    }
}
