package core.lights;
import com.jogamp.opengl.GL3;

import core.camera.Camera;
import gmaths.Mat4;
import gmaths.Vec3; 
/**
 * Spotlight class
 * Extending the Light class
 */
public class Spotlight extends Light{

  private Vec3 direction = new Vec3(1, 0.1f, 0);
  private final float cutOff = (float) Math.cos(Math.toRadians(12.5f));
  private final float outerCutOff = (float) Math.cos(Math.toRadians(17.5f));

  // private Camera camera;

  public Spotlight(GL3 gl, Camera camera, Mat4 modelMatrix){
    super(gl, camera);

    this.setIntensity(1f);
    
    //spotlight does not have ambient
    this.getMaterial().setAmbient(0f,0f,0f);
    // this.setModelMatrix(modelMatrix);
  }
  public void render(GL3 gl, Mat4 modelMatrix){
    super.render(gl, modelMatrix);

    // System.out.println("Spotlight position" + getPosition());
  }

  public void setDirection(Vec3 dir){
    this.direction = dir;
  }

  public float getCutOff(){
    return cutOff;
  }
  public float getOuterCutOff() {
    return outerCutOff;
  }
  public Vec3 getDirection(){
    return direction;
  }
}
