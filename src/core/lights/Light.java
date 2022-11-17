package core.lights;
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

import core.camera.Camera;
import core.objects.base.Cube;
import core.objects.base.Sphere;
import core.rendering.Material;
import core.rendering.Shader;
  
public class Light {
  public static final float DEFAULT_INTENS_L1 = 0.4f;
  public static final float DEFAULT_INTENS_L2 = 0.4f;
  public static final float DEFAULT_LIGHT_SIZE = 0.3f;
  
  public static final Vec3 DEFAULT_POSITION_1 = new Vec3(-8 + DEFAULT_LIGHT_SIZE/2f, DEFAULT_LIGHT_SIZE/2f, 8);
  public static final Vec3 DEFAULT_POSITION_2 = new Vec3(8 - DEFAULT_LIGHT_SIZE/2f, 16, -8 + DEFAULT_LIGHT_SIZE/2f);
  //general light properties
  public static final Vec3 LIGHT_AMBIENT = new Vec3(0.3f, 0.3f, 0.3f);
  public static final Vec3 LIGHT_DIFFUSE = new Vec3(1f, 0.87f, 0.7f);
  public static final Vec3 LIGHT_SPECULAR = new Vec3(0.8f, 0.8f, 0.8f);

  private Material material;
  private float intensity;

  private Vec3 position;
  private Mat4 modelMatrix;
  private Shader shader;
  private Camera camera;
  //private Mat4 perspective;
    
  public Light(GL3 gl, Camera camera) {
    shader = new Shader(gl, "vs_light.glsl", "fs_light.glsl");
    material = new Material();

    material.setAmbient(LIGHT_AMBIENT);
    material.setDiffuse(LIGHT_DIFFUSE);
    material.setSpecular(LIGHT_SPECULAR);

    position = new Vec3(0f,0f,0f);

    modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(0.3f,0.3f,0.3f),  modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(position), modelMatrix);

    this.camera = camera;

    fillBuffers(gl);
  }
  
  public void setModelMatrix(Mat4 modelMatrix){
    this.modelMatrix = modelMatrix;
  }

  public void setPosition(Vec3 v) {
    position.x = v.x;
    position.y = v.y;
    position.z = v.z;

    modelMatrix = Mat4.multiply(Mat4Transform.translate(v), modelMatrix);
  }
  
  public void setPosition(float x, float y, float z) {
    position.x = x;
    position.y = y;
    position.z = z;
  }
  
  public Vec3 getPosition() {
    return position;
  }
  
  public void setMaterial(Material m) {
    material = m;
  }
  
  public Material getMaterial() {
    return material;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void render(GL3 gl, Mat4 modelMatrix) {
    // modelMatrix = new Mat4(1);
    // modelMatrix = Mat4.multiply(Mat4Transform.scale(0.3f,0.3f,0.3f),  modelMatrix);
    // modelMatrix = Mat4.multiply(Mat4Transform.translate(position), modelMatrix);
    
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    
    shader.use(gl);
    shader.setVec3(gl, "lightIntensity", new Vec3(intensity, intensity, intensity));
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
  
    gl.glBindVertexArray(vertexArrayId[0]);
    
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void render(GL3 gl){
    render(gl, modelMatrix);
  }

  public void dispose(GL3 gl) {
    gl.glDeleteBuffers(1, vertexBufferId, 0);
    gl.glDeleteVertexArrays(1, vertexArrayId, 0);
    gl.glDeleteBuffers(1, elementBufferId, 0);
  }

  public void setIntensity(float intensity){
    this.intensity = intensity;
    material.setAmbient(intensity * LIGHT_AMBIENT.x, intensity * LIGHT_AMBIENT.y, intensity * LIGHT_AMBIENT.z);
    material.setDiffuse(intensity * LIGHT_DIFFUSE.x, intensity * LIGHT_DIFFUSE.y, intensity * LIGHT_DIFFUSE.z);
    material.setSpecular(intensity * LIGHT_SPECULAR.x, intensity * LIGHT_SPECULAR.y, intensity * LIGHT_SPECULAR.z);
  }
    // ***************************************************
  /* THE DATA
   */
  
  // Use a Sphere
  private float[] vertices = Sphere.VERTICES.clone();
  private int[] indices = Sphere.INDICES.clone();
    
  private int vertexStride = 8;
  private int vertexXYZFloats = 3;
  
  // ***************************************************
  /* THE LIGHT BUFFERS
   */

  private int[] vertexBufferId = new int[1];
  private int[] vertexArrayId = new int[1];
  private int[] elementBufferId = new int[1];
    
  private void fillBuffers(GL3 gl) {
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    
    gl.glBufferData(GL.GL_ARRAY_BUFFER, (long) Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);
    
    int offset = 0;
    gl.glVertexAttribPointer(0, vertexXYZFloats, GL.GL_FLOAT, false, vertexStride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);
     
    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, (long) Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
    // gl.glBindVertexArray(0);  // remove this so shader can be validated. Should be ok as any new object will bind its own VAO
  } 
}