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

    public static Interfaces.OrientationCalc holdHeading(){

        return new Interfaces.OrientationCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public double CalcOrientation(Interfaces.MoveData d){
                return 0.0;
            }
        };
    }
    public static Interfaces.OrientationCalc spinSpeed(double speed){

        return new Interfaces.OrientationCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public double CalcOrientation(Interfaces.MoveData d){
                return speed;
            }
        };
    }
}
