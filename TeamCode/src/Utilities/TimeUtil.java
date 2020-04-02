package Utilities;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class TimeUtil {

    private long startTime;
    private int millis;

    public TimeUtil() { }

    public void startTimer(int millis) {
        startTime = System.currentTimeMillis();
        this.millis = millis;
    }

    public void resetTimer() {
        startTime = System.currentTimeMillis();
    }

    public double timeRemaining(){
       return millis-(System.currentTimeMillis() - startTime);
    }

    public double timePassed(){
        return System.currentTimeMillis()-startTime;
    }

//    public void sleep(long millis) throws InterruptedException{
//        try {
//            Thread.sleep(millis);
//        } catch (Error e) {
//            e.printStackTrace();
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    public boolean timerDone() {
        return startTime + millis < System.currentTimeMillis();
    }

}