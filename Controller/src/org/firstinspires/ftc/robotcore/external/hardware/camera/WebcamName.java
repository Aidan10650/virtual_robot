package org.firstinspires.ftc.robotcore.external.hardware.camera;

import com.qualcomm.robotcore.hardware.HardwareDevice;

public interface WebcamName extends HardwareDevice
{
    /**
     * Returns the USB serial number of the webcam
     * @return the USB serial number of the webcam
     */

    /**
     * Returns the USB device path currently associated with this webcam.
     * May be null if the webcam is not presently attached.
     *
     * @return returns the USB device path associated with this name.
     * @see UsbManager#getDeviceList()
     */

    /**
     * Returns whether this camera currently attached to the robot controller
     * @return whether this camera currently attached to the robot controller
     */
    boolean isAttached();
}
