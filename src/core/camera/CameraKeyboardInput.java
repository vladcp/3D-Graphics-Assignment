package core.camera;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Camera control taken from COM3503 Online Tutorial Materials
 * by Dr Steve Maddock at The University of Sheffield, 2021.
 *
 *
 * @author Vlad Prisacariu
 */
public class CameraKeyboardInput extends KeyAdapter {
  private Camera camera;

  public CameraKeyboardInput(Camera camera){
      this.camera = camera;
  }

  public void keyPressed(KeyEvent e){
      Camera.Movement m = Camera.Movement.NO_MOVEMENT;

      switch (e.getKeyCode()) {
          case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
          case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
          case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
          case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
          case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
          case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
      }
      camera.keyboardInput(m);
  }
}
