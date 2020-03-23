package Hardware;

//import com.qualcomm.hardware.kauailabs.NavxMicroNavigationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

public class NavX {
    public double getYaw(){
        return 0.0;
    }
    /*
    private static NavX instance = null;
    private NavxMicroNavigationSensor imu = null;
    public double startAng, startPitch;
    HardwareMap hw;
    boolean first = true;

    public NavX(HardwareMap hw, String name, double startAng){
        imu = hw.get(NavxMicroNavigationSensor.class, name);
        this.startAng = startAng;
        this.hw = hw;
    }

    public static NavX getInstance(HardwareMap hw, String name){
        if(instance == null){
            instance = new NavX(hw, name, 0);
        }
        return instance;
    }

    public double getYaw(){
        try{
            if (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle == 0.0 && first) {
                first = false;
            }
            return startAng - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
        } catch(Exception e){
            android.util.Log.d("Robot", "GYRO ERROR: " + e.getMessage());
            return getYaw();
        }
    }

    public double getPitch() {
        try{
            return startPitch - (imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle * -1);
        } catch (Exception e) {
            android.util.Log.d("Robot", "GYRO ERROR: " + e.getMessage());
            return getPitch();
        }
    }

    public void resetPitch() {
        startPitch = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).secondAngle;
    }

    public double getRaw() {
        return -imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public void close(){
        imu = null;
        instance = null;
    }

    public double getLatency(){
        return (System.nanoTime() - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).acquisitionTime)/1000000;
    }

    public double getUpdatedYaw(){
        while((System.nanoTime() - imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).acquisitionTime)/1000000 > 5){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle;
    }

    public double getOutput() {
        return getYaw();
    }
    */

}