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

  private Vec3 direction;
  private final float cutOff = (float) Math.cos(Math.toRadians(12.5f));
  private final float outerCutOff = (float) Math.cos(Math.toRadians(17.5f));
  private float intensity = 1f;
  // private Camera camera;

  public Spotlight(GL3 gl, Camera camera, Mat4 modelMatrix){
    super(gl, camera);

    this.setIntensity(intensity);
    
    //spotlight does not have ambient
    this.getMaterial().setAmbient(0f,0f,0f);
    // this.setModelMatrix(modelMatrix);
  }
  public void render(GL3 gl, Mat4 modelMatrix){
    super.render(gl, modelMatrix);

    // System.out.println("Spotlight position" + getPosition());
  }

  public void setIntensity(float intensity) {
    this.getMaterial().setDiffuse(intensity * LIGHT_DIFFUSE.x, intensity * LIGHT_DIFFUSE.y, intensity * LIGHT_DIFFUSE.z);
    this.getMaterial().setSpecular(intensity * LIGHT_SPECULAR.x, intensity * LIGHT_SPECULAR.y, intensity * LIGHT_SPECULAR.z);
  }

  public void toggle() {
    intensity = 1 - intensity;
    this.setIntensity(intensity);
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
