package org.firstinspires.ftc.teamcode.ftc16072;

import Calculators.*;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "ComplexTele", group = "ftc10650")
public class MecanumDrivingComplexOpMode extends Op.ComplexOp {
    private MecanumDrive mecanumDrive = new MecanumDrive();

    @Override
    public void body() throws InterruptedException {
        ComplexMove(SpeedCalcs.topSpeed(1), MotionCalcs.moveWithFieldCentricJoystick(),
                OrientationCalcs.turnWithJoystick(), OtherCalcs.TeleOpMatch());
    }
}
