package core.rendering;

import gmaths.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.texture.*;

import core.camera.Camera;
import core.lights.Light;
import core.lights.Spotlight;

public class Model {
  
  private Mesh mesh;
  private Texture textureId1; 
  private Texture textureId2; 
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private Light light1;
  private Light light2;

  // TODO Spotlight
  private Spotlight spotlight1;
  private Spotlight spotlight2;
  
  // Used for texture animation
  private float offsetX;
  private boolean offsetExists = false;
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Spotlight spotlight1, Spotlight spotlight2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1, Texture textureId2) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight1 = spotlight1;
    this.spotlight2 = spotlight2;

    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
  }
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Spotlight spotlight1, Spotlight spotlight2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, Texture textureId1) {
    this(gl, camera, light1, light2, spotlight1, spotlight2, shader, material, modelMatrix, mesh, textureId1, null);
  }
  
  public Model(GL3 gl, Camera camera, Light light1, Light light2, Spotlight spotlight1, Spotlight spotlight2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, light1, light2, spotlight1, spotlight2, shader, material, modelMatrix, mesh, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setOffsetX(float offsetX){
    this.offsetX = offsetX;
    this.offsetExists = true;
  }
  // public void setLight(Light light) {
  //   this.light = light;
  // }
  // private Vec3 currentPosition;

  public void render(GL3 gl, Mat4 modelMatrix) {
    // if(modelMatrix != this.modelMatrix) {
      this.modelMatrix = modelMatrix;
    // } 
    // System.out.println("CURRENT POSITION: :" + this.modelMatrix.getLastColumn());

    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));

    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    
    shader.setVec3(gl, "viewPos", camera.getPosition());

    shader.setVec3(gl, "light1.position", light1.getPosition());
    shader.setVec3(gl, "light1.ambient", light1.getMaterial().getAmbient());
    shader.setVec3(gl, "light1.diffuse", light1.getMaterial().getDiffuse());
    shader.setVec3(gl, "light1.specular", light1.getMaterial().getSpecular());

    shader.setVec3(gl, "light2.position", light2.getPosition());
    shader.setVec3(gl, "light2.ambient", light2.getMaterial().getAmbient());
    shader.setVec3(gl, "light2.diffuse", light2.getMaterial().getDiffuse());
    shader.setVec3(gl, "light2.specular", light2.getMaterial().getSpecular());

    //TODO Spotlight(s)
    shader.setVec3(gl, "spotlight1.position", spotlight1.getPosition());
    shader.setVec3(gl, "spotlight1.direction", spotlight1.getDirection());
    
    shader.setVec3(gl, "spotlight1.ambient", spotlight1.getMaterial().getAmbient());
    shader.setVec3(gl, "spotlight1.diffuse", spotlight1.getMaterial().getDiffuse());
    shader.setVec3(gl, "spotlight1.specular", spotlight1.getMaterial().getSpecular());
    shader.setFloat(gl, "spotlight1.cutOff", spotlight1.getCutOff());
    shader.setFloat(gl, "spotlight1.outerCutOff", spotlight1.getOuterCutOff());

    shader.setVec3(gl, "spotlight2.position", spotlight2.getPosition());
    shader.setVec3(gl, "spotlight2.direction", spotlight2.getDirection());

    shader.setVec3(gl, "spotlight2.ambient", spotlight2.getMaterial().getAmbient());
    shader.setVec3(gl, "spotlight2.diffuse", spotlight2.getMaterial().getDiffuse());
    shader.setVec3(gl, "spotlight2.specular", spotlight2.getMaterial().getSpecular());
    shader.setFloat(gl, "spotlight2.cutOff", spotlight2.getCutOff());
    shader.setFloat(gl, "spotlight2.outerCutOff", spotlight2.getOuterCutOff());

    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());

    if (textureId1!=null) {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      textureId1.bind(gl);  // uses JOGL Texture class
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      textureId2.bind(gl);  // uses JOGL Texture class
    } 

    if (offsetExists) shader.setFloat(gl, "offset", this.offsetX, 0); //for texture animation
    mesh.render(gl);
    //reset 
    gl.glActiveTexture(GL.GL_TEXTURE0);
  } 
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) textureId1.destroy(gl);
    if (textureId2!=null) textureId2.destroy(gl);
  }
  
  public Mat4 getModelMatrix(){
    return this.modelMatrix;
  }

  public Vec3 getCurrentPosition() {
    return this.modelMatrix.getLastColumn();
  }
}
