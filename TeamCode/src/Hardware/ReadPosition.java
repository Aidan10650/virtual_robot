package Hardware;

import Utilities.*;

public class ReadPosition {

    double currYSpeed = 0;
    double currXSpeed = 0;
    double currTurnSpeed = 0;
    public MathUtil.Point currCoordinate;

    /************************
     * Insert Odometry Here *
     ************************/
    public static MathUtil.Point getPosition(){
        MathUtil.Point point = new MathUtil.Point(0,0);
        return point;
    }




}
