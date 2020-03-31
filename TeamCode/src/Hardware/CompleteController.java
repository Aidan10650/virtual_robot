package Hardware;

/**
 * Created by Varun on 11/16/2017.
 * Refactored and Edited by Aidan on 3/14/2020.
 */


import Utilities.Vector2D;
import com.qualcomm.robotcore.hardware.GamePad;

import Utilities.MathUtil;

public class CompleteController {
    GamePad gamepad;
    private class StickConfig {
        JoystickDeadzoneShape ds;
        JoystickShape js;
        private boolean reverseX;
        private boolean reverseY;
        public double JoystickDeadzoneMag;

        StickConfig(JoystickDeadzoneShape ds, JoystickShape js, boolean reverseX, boolean reverseY, double JoystickDeadzoneMag){
            this.ds = ds;
            this.js = js;
            this.reverseX = reverseX;
            this.reverseY = reverseY;
            this.JoystickDeadzoneMag = JoystickDeadzoneMag;
        }
    }

    StickConfig rightStick;
    StickConfig leftStick;

    private class TrigConfig {
        private double TD;
        private boolean RT;
        TrigConfig(double triggerDeadzone, boolean reverseTrigger){
            this.TD = triggerDeadzone;
            this.RT = reverseTrigger;
        }
    }

    TrigConfig rightTrig;
    TrigConfig leftTrig;

    public void CompleteController(GamePad gamepad){
        this.gamepad = gamepad;
        rightStick = new StickConfig(JoystickDeadzoneShape.CIRCULAR,JoystickShape.CIRCULAR,false,true,0.01);
        leftStick = new StickConfig(JoystickDeadzoneShape.CIRCULAR,JoystickShape.SQUARE,false,true,0.01);
        rightTrig = new TrigConfig(0.01,false);
        leftTrig = new TrigConfig(0.01,false);

    }

    public enum JoystickDeadzoneShape{
        CIRCULAR,
        SQUARE
    }

    public enum JoystickShape{
        CIRCULAR,
        SQUARE
    }

    public void SetRightControllerJoystick (JoystickShape js, JoystickDeadzoneShape ds, double JDM, boolean reverseX, boolean reverseY){
        rightStick = new StickConfig(ds,js,reverseX,reverseY,JDM);
    }

    public void SetLeftControllerJoystick (JoystickShape js, JoystickDeadzoneShape ds, double JDM, boolean reverseX, boolean reverseY){
        leftStick = new StickConfig(ds,js,reverseX,reverseY,JDM);
    }


    public void SetRightControllerTrigger (double TriggerDeadzone, boolean ReverseTrigger){
        rightTrig = new TrigConfig(TriggerDeadzone, ReverseTrigger);
    }

    public void SetLeftLeftControllerTrigger (double TriggerDeadzone, boolean ReverseTrigger){
        leftTrig = new TrigConfig(TriggerDeadzone, ReverseTrigger);
    }


    public Vector2D getLeftStick(){
        return GetControllerJoystick(gamepad.left_stick_x,gamepad.left_stick_y,leftStick);
    }

    public Vector2D getRightStick(){
        return GetControllerJoystick(gamepad.right_stick_x,gamepad.right_stick_y,leftStick);
    }


    public double leftTrigger(){
        return GetControllerTrigger(gamepad.left_trigger,leftTrig);
    }

    public double rightTrigger(){
        return GetControllerTrigger(gamepad.right_trigger,rightTrig);
    }

    public boolean leftStickButton(){
        return gamepad.left_stick_button;
    }

    public boolean rightStickButton(){
        return gamepad.right_stick_button;
    }

    public boolean leftBumper(){
        return gamepad.left_bumper;
    }

    public boolean rightBumper(){
        return gamepad.right_bumper;
    }

    public boolean dpadUp(){
        return gamepad.dpad_up;
    }

    public boolean dpadDown(){
        return gamepad.dpad_down;
    }

    public boolean dpadLeft(){
        return gamepad.dpad_left;
    }

    public boolean dpadRight(){
        return gamepad.dpad_right;
    }

    public boolean backButton() {
        return gamepad.back;
    }

    public boolean a(){
        return gamepad.a;
    }

    public boolean b(){
        return gamepad.b;
    }

    public boolean x(){
        return gamepad.x;
    }

    public boolean y(){
        return gamepad.y;
    }

    private Vector2D GetControllerJoystick (double X, double Y, StickConfig s){
        Vector2D XY = new Vector2D();
        boolean OutsideJoystickDeadzone = false;
        double JDM = s.JoystickDeadzoneMag;
        if(s.reverseX) X=-X;
        if(s.reverseY) Y=-Y;
        switch(s.ds){
            case CIRCULAR:  if (JDM > MathUtil.Distance(X,Y,0,0)){
                                OutsideJoystickDeadzone = false;
                            } else {
                                OutsideJoystickDeadzone = true;
                            }
                            break;

            case SQUARE:    if (X < JDM || Y < JDM){
                                OutsideJoystickDeadzone = false;
                            } else{
                                OutsideJoystickDeadzone = true;
                            }
                            break;
        }
        if (OutsideJoystickDeadzone == true) {
            switch(s.js){

                case CIRCULAR:  XY.x = X;
                                XY.y = Y;
                                break;

                //Square joystick shape means that the maximum output of a joystick lies on a square that fits inside the circle
                //This allows for strafing diagonally at maximum speed
                case SQUARE:    XY.x = X * Math.sqrt(2);
                                XY.y = Y * Math.sqrt(2);

                                if (XY.x > 1 || XY.x < -1) {
                                    XY.x = MathUtil.getSign(XY.x);
                                } else if (XY.y > 1 || XY.y < -1) {
                                    XY.y = MathUtil.getSign(XY.y);
                                }
                                break;
            }
        }
        return XY;
    }
    private double GetControllerTrigger (double TriggerPos, TrigConfig t){
        double rval;
        if(TriggerPos >= t.TD){
            rval = TriggerPos-t.TD;
        } else {
            rval = 0;
        }
        rval = rval/(1-t.TD);
        if(t.RT) {
            rval = 1 - rval;
        }

        return rval;
    }
}