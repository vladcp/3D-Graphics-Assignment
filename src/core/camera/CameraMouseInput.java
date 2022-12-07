package core.camera;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Class taken from COM3503 Online Tutorial 
 * by Dr Steve Maddock at The University of Sheffield, 2022.
 *
 */
public class CameraMouseInput extends MouseMotionAdapter {
    private Point lastPoint;
    private Camera camera;

    public CameraMouseInput(Camera camera){
        this.camera = camera;
    }

    public void mouseDragged(MouseEvent e) {
        Point ms = e.getPoint();

        float sensitivity = 0.001f;
        float dx=(float) (ms.x-lastPoint.x)*sensitivity;
        float dy=(float) (ms.y-lastPoint.y)*sensitivity;

        if (e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
            camera.updateYawPitch(dx, -dy);

        lastPoint = ms;
    }
    public void mouseMoved(MouseEvent e) {
        lastPoint = e.getPoint();
    }
}
