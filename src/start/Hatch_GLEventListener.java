package start;

import java.util.List;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.texture.Texture;

import core.camera.Camera;
import core.lights.Light;
import core.lights.Spotlight;
import core.objects.final_objects.Egg;
import core.objects.final_objects.Room;
import core.objects.final_objects.Table;
import core.objects.final_objects.alien_lamps.AlienLamp;
import core.rendering.Material;
import core.rendering.Mesh;
import core.rendering.Model;
import core.rendering.Shader;
import core.rendering.TextureLibrary;
import gmaths.*;
import static core.constants.Constants.*;

public class Hatch_GLEventListener implements GLEventListener {

  // private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;

  private double startTime;

  public Hatch_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f, 6f, 15f));
    this.camera.setTarget(new Vec3(0f, 5f, 0f));
  }

  public double getCurrentSeconds() {
    return System.currentTimeMillis() / 1000.0;
  }

  public double getElapsedTime() {
    return getCurrentSeconds() - startTime;
  }

  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */
  @Override
  public void init(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());

    // Open GL settings
    initOpenGL(gl);

    // Initialise
    initialise(gl);
  }

  @Override
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float) width / (float) height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /*
   * Draw
   */
  @Override
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  @Override
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light1.dispose(gl);
    light2.dispose(gl);
    room.dispose(gl);
    table.dispose(gl);
    egg.dispose(gl);
  }

  private void initOpenGL(GL3 gl) {
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW); // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    gl.glCullFace(GL.GL_BACK);
  }

  // ***************************************************
  /*
   * THE SCENE
   * All the methods to handle the scene.
   */
  private Room room;
  private Table table;
  private AlienLamp alienLamp1;
  private AlienLamp alienLamp2;

  private Egg egg;

  private Light light1;
  private Light light2;
  private Spotlight spotlight1;
  private Spotlight spotlight2;

  // private final int T_CONTAINER_DIFFUSE = 0;
  // private final int T_CONTAINER_SPECULAR = 1;

  private void initialise(GL3 gl) {
    List<Shader> shaderList = Shader.populateShaderList(gl);
    List<Texture> textureList = TextureLibrary.populateTextureList(gl);
    List<Mesh> meshList = Mesh.populateMeshList(gl);

    initialiseLights(gl); // needs to be done first

    initialiseScene(gl, textureList, shaderList, meshList);
  }

  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    // Render Lights
    light1.render(gl);
    light2.render(gl);

    // animate lamp
    alienLamp1.draw(gl);
    alienLamp2.draw(gl);

    room.render(gl);
    room.getWindow().animateTexture(getCurrentSeconds() - startTime);

    table.render(gl);
    egg.render(gl);
    egg.animate(getElapsedTime());
  }

  public void animateLamp1() {
    alienLamp1.setIsAnimating(true);
  }

  public AlienLamp getLamp1() {
    return alienLamp1;
  }

  public AlienLamp getLamp2() {
    return alienLamp2;
  }

  public Light getLight1() {
    return light1;
  }

  public Light getLight2() {
    return light2;
  }

  public Spotlight getSpotlight1() {
    return spotlight1;
  }

  public Spotlight getSpotlight2() {
    return spotlight2;
  }

  private void initialiseScene(GL3 gl, List<Texture> textureList, List<Shader> shaderList, List<Mesh> meshList) {
    spotlight1 = new Spotlight(gl, camera, null);
    spotlight2 = new Spotlight(gl, camera, null);
    alienLamp1 = new AlienLamp(gl, camera, light1, light2, spotlight1, spotlight2, meshList.get(1),
        meshList.get(2), textureList.get(TextureLibrary.WALL_TEXTURE), shaderList.get(Shader.SINGLE_TEXTURE),
        "AlienLamp1");
    alienLamp2 = new AlienLamp(gl, camera, light1, light2, spotlight1, spotlight2, meshList.get(1),
        meshList.get(2), textureList.get(TextureLibrary.WALL_TEXTURE), shaderList.get(Shader.SINGLE_TEXTURE),
        "AlienLamp2");

    room = new Room(gl, camera, light1, light2, spotlight1, spotlight2, shaderList.get(Shader.SINGLE_TEXTURE),
        shaderList.get(Shader.ANIMATED_TEXTURE),
        shaderList.get(Shader.STATIC_NOLIGHT), textureList.get(TextureLibrary.WALL_TEXTURE),
        textureList.get(TextureLibrary.FLOOR_TEXTURE),
        textureList.get(TextureLibrary.WINDOW_GROUND),
        textureList.get(TextureLibrary.WINDOW_CLOUDS));
    table = new Table(gl, camera, light1, light2, spotlight1, spotlight2, shaderList.get(Shader.SINGLE_TEXTURE),
        textureList.get(TextureLibrary.WALL_TEXTURE));

    Material mat = new Material();
    Model eggModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2,
        shaderList.get(Shader.DOUBLE_TEXTURE), mat, null, meshList.get(2), textureList.get(TextureLibrary.EGG),
        textureList.get(TextureLibrary.EGG_SPECULAR));
    Model baseEggModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2,
        shaderList.get(Shader.SINGLE_TEXTURE), mat, null, meshList.get(1), textureList.get(TextureLibrary.CONTAINER));
    egg = new Egg(eggModel, baseEggModel);

  }

  // initialise global lights
  private void initialiseLights(GL3 gl) {
    light1 = new Light(gl, camera);
    light2 = new Light(gl, camera);
    light1.setIntensity(Light.DEFAULT_INTENS_L1);
    // light1.setIntensity(0f);
    light2.setIntensity(Light.DEFAULT_INTENS_L2);
    // light2.setIntensity(0f);

    light1.setPosition(DEFAULT_POSITION_1);
    light2.setPosition(DEFAULT_POSITION_2);
  }
}
