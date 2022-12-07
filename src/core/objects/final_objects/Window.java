package core.objects.final_objects;

import core.rendering.Model;
import gmaths.Mat4;
import gmaths.Mat4Transform;

import static core.Constants.*;

import com.jogamp.opengl.GL3;
/**
 * Window class. Contains model matrices for the moving sky
 * and for the stationary ground underneath
 * 
 * @author Vlad Prisacariu, 2022
 */
public class Window {
  //to animate the texture
  private Model sky_model, ground_model;

  public Window(Model window_sky_model, Model window_ground_model){
    this.sky_model = window_sky_model;
    this.ground_model = window_ground_model;

    sky_model.setModelMatrix(getSkyModelMatrix());
    ground_model.setModelMatrix(getGroundModelMatrix());
  }
  // sky is on top, and moving
  private Mat4 getSkyModelMatrix() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(WALL_SIZE*2,1f,WALL_SIZE*0.8f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,WALL_SIZE*0.75f,-WALL_SIZE*0.75f), modelMatrix);
    return modelMatrix;
  }
  // ground is under sky
  private Mat4 getGroundModelMatrix() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(WALL_SIZE*2,1f,WALL_SIZE*0.8f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,-WALL_SIZE*0.05f,-WALL_SIZE*0.75f), modelMatrix);
    return modelMatrix;
  }

  public void render(GL3 gl){
    sky_model.render(gl);
    ground_model.render(gl);
  }

  public void dispose(GL3 gl){
    this.sky_model.dispose(gl);
    this.ground_model.dispose(gl);
  }
  
  /**
   * Method which calculates and saves the X offset, which is
   * used to animate the textures.
   *
   * @param elapsedTime time elapsed since the start of program
   */
  public void animateTexture(double elapsedTime){
    final double SPEED = 0.03;
    double increment = elapsedTime * SPEED;

    this.sky_model.setOffsetX(
            (float)(increment - Math.floor(increment)));
}
}
