package start;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import core.camera.Camera;
import core.camera.CameraKeyboardInput;
import core.camera.CameraMouseInput;
import core.objects.final_objects.alien_lamps.AlienLamp;
import ui.GUI;

public class Hatch extends JFrame {
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  
  private GLCanvas canvas;
  private Hatch_GLEventListener glEventListener;
  private static final int TARGET_FPS = 60;
  private FPSAnimator animator; 

  private Camera camera;

  /*
   * Entry point of the application
   */
  public static void main(String[] args) {
    Hatch program = new Hatch("Scene");
    program.getContentPane().setPreferredSize(dimension);
    program.pack();
    program.setVisible(true);
    program.canvas.requestFocusInWindow();
  }

  public Hatch(String titleBarText){
    super(titleBarText);

    // Initialise OpenGL
    initialiseGL();

    // Initialise Camera
    initialiseCamera();

    // Add listeners
    addListeners();

    //add UI 
    GUI gui = new GUI(this.glEventListener); // IMPORTANT
    this.add(gui, BorderLayout.SOUTH);
  }

  public void printClassLoaders() {
    System.out.println("Classloader of HATCH:"
        + Hatch.class.getClassLoader());
    System.out.println("Classloader of Hatch_GLEventListener:"
        + Hatch_GLEventListener.class.getClassLoader());
    System.out.println("Classloader of GUI:"
        + GUI.class.getClassLoader());
    System.out.println("Classloader of AlienLamp:"
        + AlienLamp.class.getClassLoader());
}

  public Hatch_GLEventListener getGLEventListener() {
    return glEventListener;
  }

  /*
   * Add listeners
   */
  private void addListeners(){
    canvas.addGLEventListener(glEventListener);
    canvas.addKeyListener(new CameraKeyboardInput(camera));
    canvas.addMouseMotionListener(new CameraMouseInput(camera));

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
  }

  /*
   * Set up camera
   */
  private void initialiseCamera() {
    camera = new Camera(Camera.DEFAULT_POSITION, 
        Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new Hatch_GLEventListener(camera);
  }
  /*
   * Initialise GL
   */
  private void initialiseGL() {
    GLCapabilities glCapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    
    // Initialise canvas
    canvas = new GLCanvas(glCapabilities);
    getContentPane().add(canvas, BorderLayout.CENTER);

    // Configure animator
    animator = new FPSAnimator(canvas, TARGET_FPS);
    animator.start();
  }
}
