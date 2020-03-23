package Control.TeleControl;

import com.qualcomm.robotcore.hardware.GamePad;

import Utilities.MathUtil;
import Hardware.NavX;

/**
 *   Created by Aidan 9/14/19
 */

public class FieldBasedControl {

    public static MathUtil.Vector getJoystick(MathUtil.Vector point, double heading){
        MathUtil.Vector stick  = new MathUtil.Vector(point.x, point.y);

        float gyroAngle = (float)heading;

        return MathUtil.Rotate2D(stick, -gyroAngle);
    }

    public static MathUtil.Vector getJoystick(MathUtil.Vector point, NavX gyro){
        MathUtil.Vector stick  = new MathUtil.Vector(point.x, point.y);

        float gyroAngle = (float)gyro.getYaw();

        return MathUtil.Rotate2D(stick, -gyroAngle);
    }

    public static MathUtil.Point getLeftJoystick(GamePad driver, NavX gyro){
        MathUtil.Point stick  = new MathUtil.Point(driver.left_stick_x, driver.left_stick_y);

        float gyroAngle = (float)gyro.getYaw();

        return MathUtil.Rotate2D(stick, -gyroAngle);

    }



    public static MathUtil.Point getRightJoystick(GamePad driver, NavX gyro){
        MathUtil.Point stick = new MathUtil.Point(driver.right_stick_x, driver.right_stick_y);

        float gyroAngle = (float)gyro.getYaw();

        return MathUtil.Rotate2D(stick, -gyroAngle);

    }
}
