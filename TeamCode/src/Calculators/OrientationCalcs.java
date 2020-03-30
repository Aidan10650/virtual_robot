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

    public static Interfaces.OrientationCalc spinToProgress(spinProgress... spinData){

        return new Interfaces.OrientationCalc(){
            @Override
            public boolean doProgress(Interfaces.MoveData d) {
                return false;
            }

            @Override
            public double CalcOrientation(Interfaces.MoveData d){

                for(int i = 0 ; i < spinData.length ; i++){
                    if(d.progress>=spinData[i].startSpinProgress && d.progress<=spinData[i].endSpinProgress){
                        d.currentSpin = i;
                        d.foundSpin = true;
                    }
                }

                if(d.foundSpin){

                    double currSpinProgress = (d.progress - spinData[d.currentSpin].startSpinProgress)/
                            (spinData[d.currentSpin].endSpinProgress-spinData[d.currentSpin].startSpinProgress);
                    double currSpinTo = currSpinProgress*spinData[d.currentSpin].spinTo;
                    d.orientationError = currSpinTo-d.heading;
                } else {
                    d.orientationError = 0;
                }

                d.foundSpin = false;
                return -d.orientationError*d.orientationP;
            }
        };
    }
    public static class spinProgress{
        double startSpinProgress;
        double endSpinProgress;
        double spinTo;
        public spinProgress(double startSpinProgress, double endSpinProgress, double spinTo){
            this.startSpinProgress = startSpinProgress;
            this.endSpinProgress = endSpinProgress;
            this.spinTo = spinTo;
        }
    }
}
