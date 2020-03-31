package org.firstinspires.ftc.teamcode.ftc16072;

import Calculators.MotionCalcs;
import Calculators.OrientationCalcs;
import Calculators.OtherCalcs;
import Calculators.SpeedCalcs;
import Utilities.Vector2D;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "ComplexAuto", group = "ftc10650")
public class MecanumDrivingComplexAutoMode extends Op.ComplexOp {

    @Override
    public void body() throws InterruptedException {
        ComplexMove(
                SpeedCalcs.setSpeed(1),
                MotionCalcs.PointMotion(25,
                        new Vector2D(-150, 150),
                        new Vector2D(-300, 0),
                        new Vector2D(-150, -150),
                        new Vector2D(0, 0)),
                OrientationCalcs.spinToProgress(
                        new OrientationCalcs.spinProgress(0.15, 0.2, 90),
                        new OrientationCalcs.spinProgress(0.75, 0.85, 0)),
                OtherCalcs.nothing());
        ComplexMove(
                SpeedCalcs.setSpeed(1),
                MotionCalcs.PointMotion(25,
                        new Vector2D(-150, 150),
                        new Vector2D(-300, 0),
                        new Vector2D(-150, -150),
                        new Vector2D(0, 0)),
                OrientationCalcs.spinToProgress(
                        new OrientationCalcs.spinProgress(0.1, 0.2, 90),
                        new OrientationCalcs.spinProgress(0.75, 0.85, -90)),
                OtherCalcs.nothing());
    }
}
