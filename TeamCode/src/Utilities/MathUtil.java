package Utilities;

public class MathUtil {

    private enum sign{
        POSITIVE,
        NEGATIVE
    }

    public static double getSign (double val){
        return val/Math.abs(val);
    }

    public static double CompleteScaleAdjust(double fitValue, double... val){
        //makes the highest value always equal 1 while proportionally moving the others

        double largestValue = 0;
        double adjustment;

        for(int i = 0; i < val.length; i++){
            largestValue = Math.max(Math.abs(val[i]),largestValue);
        }

        adjustment = (largestValue != 0) ? fitValue/largestValue : 0;

        return adjustment;
    }

    public static double ScaleAdjustment(double maxValue, double... val){
    //finds the biggest number that is greater than one and proportionally
    //reduces it and the other numbers so the greatest value is equal to one

        double largestValue = 0;
        double adjustment;

        for(int i = 0; i < val.length; i++){
            largestValue = Math.max(Math.abs(val[i]),largestValue);
        }

        adjustment = (largestValue > maxValue) ? ((largestValue == 0) ? 0 : maxValue/largestValue) : 1;

        return adjustment;
    }

    public static double Distance(double X1, double Y1, double X2, double Y2){
        return Math.sqrt(Math.pow(X1-X2,2)+Math.pow(Y1-Y2,2));
    }

    public static double[] quadSolve(double coefficientA, double coefficientB, double coefficientC){
        double A = coefficientA;
        double B = coefficientB;
        double C = coefficientC;
        double answer1 = 0;
        double answer2 = 0;
        double imaginary = Math.sqrt(-1);
        double[] zeros = {Math.sqrt(-1),Math.sqrt(-1)};

        if (Math.pow(B,2)-4*A*C < 0){
            return zeros;
        } else {
            answer1 = (-1*B+Math.sqrt(Math.pow(B,2)-4*A*C))/2*A;
            answer2 = (-1*B-Math.sqrt(Math.pow(B,2)-4*A*C))/2*A;
            zeros[0] = answer1;
            zeros[1] = answer2;
            return zeros;
        }



    }

    public static double limit(double maVal, double miVal, double input) {
        return input > maVal ? maVal : input < miVal ? miVal : input;
    }


    public static double quadEval(double x, double coefficientA, double coefficientB, double coefficientC){
        return coefficientA*(Math.pow(x,2))+coefficientB*x+coefficientC;
    }

    public static double cubicEval(double x, double coefficientA, double coefficientB, double coefficientC, double coefficientD){
        return coefficientA*(Math.pow(x,3))+coefficientB*(Math.pow(x,2))+coefficientC*x+coefficientD;
    }

    public static double ellipseEval(double x, double h, double k, double a, double b, MathUtil.sign sign){
        double y = 0;
        switch (sign) {
            case POSITIVE:
                y = (Math.sqrt((1 - Math.pow((x - h), 2) / Math.pow(a, 2)) * Math.pow(b, 2)) + k);
                break;
            case NEGATIVE:
                y = ((-1 * Math.sqrt((1 - Math.pow((x - h), 2) / Math.pow(a, 2)) * Math.pow(b, 2))) + k);
                break;
        }
        return y;
    }

    public static double circleEval(double x, double h, double k, double radius, MathUtil.sign sign){
        double y = 0;
        switch(sign){
            case POSITIVE:
                y = (Math.sqrt(Math.pow(radius,2)-(Math.pow((x-h),2))))+k;
                break;
            case NEGATIVE:
                y = -1*(Math.sqrt(Math.pow(radius,2)-(Math.pow((x-h),2))))+k;
                break;
        }
        return y;
    }

    public static double exponentialEval(double x, double h, double k, double a, double b){
        double y = a*Math.pow(b,(x-h))+k;
        return y;
    }

    public static double findMax(double... inputs){
        double largestValue = 0;
        for(int i = 0; i <= inputs.length; i++){
            largestValue = Math.max(Math.abs(inputs[i]),largestValue);
        }
        return largestValue;
    }
}
