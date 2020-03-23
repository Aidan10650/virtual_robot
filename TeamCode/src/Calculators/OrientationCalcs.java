package Calculators;

public class OrientationCalcs {

    public static Interfaces.OrientationCalc turnWithJoystick(){

        return new Interfaces.OrientationCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public double CalcOrientation(Interfaces.MoveData d){
                return d.driver.getRightStick().x;
            }
        };
    }
}
