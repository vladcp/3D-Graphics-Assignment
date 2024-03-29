package core.rendering;
import gmaths.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;  
/**
 * Shader class,
 * adapted from COM3503 Online Tutorial Materials
 * by Dr Steve Maddock at The University of Sheffield, 2022.
 */
public class Shader {
  
  private static final boolean DISPLAY_SHADERS = false;
  
  private int ID;
  private String vertexShaderSource;
  private String fragmentShaderSource;
  // private File vertexShaderSource;
  // private File fragmentShaderSource;
  
  /* The constructor */
  public Shader(GL3 gl, String vertexPath, String fragmentPath) {
    try {
      vertexShaderSource = new String(Files.readAllBytes(Paths.get("core/shaders/vertex", vertexPath)), Charset.defaultCharset());
      fragmentShaderSource = new String(Files.readAllBytes(Paths.get("core/shaders/fragment", fragmentPath)), Charset.defaultCharset());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if (DISPLAY_SHADERS) display();
    ID = compileAndLink(gl);
  }
  
  public int getID() {
    return ID;
  }
  
  public void use(GL3 gl) {
    gl.glUseProgram(ID);
  }
  
  public void setInt(GL3 gl, String name, int value) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform1i(location, value);
  }
  
  public void setFloat(GL3 gl, String name, float value) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform1f(location, value);
  }
  
  public void setFloat(GL3 gl, String name, float f1, float f2) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform2f(location, f1, f2);
  }
  
  public void setFloat(GL3 gl, String name, float f1, float f2, float f3) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform3f(location, f1, f2, f3);
  }
  
  public void setFloat(GL3 gl, String name, float f1, float f2, float f3, float f4) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform4f(location, f1, f2, f3, f4);
  }
  
  public void setFloatArray(GL3 gl, String name, float[] f) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniformMatrix4fv(location, 1, false, f, 0);
  }
  
  public void setVec3(GL3 gl, String name, Vec3 v) {
    int location = gl.glGetUniformLocation(ID, name);
    gl.glUniform3f(location, v.x, v.y, v.z);
  }
  
  private void display() {
    System.out.println("***Vertex shader***");
    System.out.println(vertexShaderSource);
    System.out.println("\n***Fragment shader***");
    System.out.println(fragmentShaderSource);
  }
  
  private int compileAndLink(GL3 gl) {
    // gl.glBindVertexArray(1);  // hack to stop link error, since a VAO needs to be bound for shader validation
    String[][] sources = new String[1][1];
    sources[0] = new String[]{ vertexShaderSource };
    ShaderCode vertexShaderCode = new ShaderCode(GL3.GL_VERTEX_SHADER, sources.length, sources);
    boolean compiled = vertexShaderCode.compile(gl, System.err);
    if (!compiled)
      System.err.println("[error] Unable to compile vertex shader: " + sources);
    sources[0] = new String[]{ fragmentShaderSource };
    ShaderCode fragmentShaderCode = new ShaderCode(GL3.GL_FRAGMENT_SHADER, sources.length, sources);
    compiled = fragmentShaderCode.compile(gl, System.err);
    if (!compiled)
      System.err.println("[error] Unable to compile fragment shader: " + sources);
    ShaderProgram program = new ShaderProgram();
    program.init(gl);
    program.add(vertexShaderCode);
    program.add(fragmentShaderCode);
    program.link(gl, System.out);
    if (!program.validateProgram(gl, System.out))
      System.err.println("[error] Unable to link program");
    return program.program();
  }

  public final static int GENERAL = 0;
  public final static int ANIM_TEX_NOLIGHT = 1;
  public final static int STATIC_NOLIGHT = 2;
  public final static int DIFF_SPEC = 3;
  /**
   * Store the program shaders in a list
   * @param gl
   * @return {@link ArrayList} containing the used shaders
   */
  public static List<Shader> loadShadersInList(GL3 gl){
    List<Shader> shaders = new ArrayList<>();
    shaders.add(new Shader(gl, "vs_general.glsl", "fs_general.glsl"));
    shaders.add(new Shader(gl, "vs_animated_texture.glsl", "fs_nolight.glsl")); //for the moving clouds
    shaders.add(new Shader(gl, "vs_general.glsl", "fs_nolight_static.glsl")); // for the window ground
    shaders.add(new Shader(gl, "vs_general.glsl", "fs_diffuse_specular.glsl")); // for diffuse, specular maps objects
    return shaders;
  }
  // public static List<Shader> populateShaderList(GL3 gl){
  //   List<Shader> shaders = new ArrayList<>();
  //   shaders.add(new Shader(gl, "./shaders/vertex/vs_general.glsl", "./shaders/fragment/fs_single_texture.glsl"));
  //   shaders.add(new Shader(gl, "./shaders/vertex/vs_animated_texture.glsl", "./shaders/fragment/fs_nolight.glsl")); //for the moving clouds
  //   shaders.add(new Shader(gl, "./shaders/vertex/vs_general.glsl", "./shaders/fragment/fs_nolight_static.glsl")); // for the window ground
  //   shaders.add(new Shader(gl, "./shaders/vertex/vs_general.glsl", "./shaders/fragment/fs_diffuse_specular.glsl")); // for diffuse, specular maps objects
  //   return shaders;
  // }
}