package Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TimeUtil {

    private static long startTime;
    private static int millis;

    public TimeUtil() {

    }

    public static void startTimer(int millis) {
        TimeUtil.millis = millis;
        startTime = System.currentTimeMillis();
    }

    public static double timeRemaining(){
       return millis-(System.currentTimeMillis() - startTime);
    }

    public static void sleep(long millis) throws InterruptedException{
        try {
            Thread.sleep(millis);
        } catch (Error e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean timerDone() {
        return startTime + millis < System.currentTimeMillis();
    }

}