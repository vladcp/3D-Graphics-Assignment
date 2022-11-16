package core.objects.final_objects;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import core.camera.Camera;
import core.lights.Light;
import core.lights.Spotlight;
import core.objects.base.Surface;
import core.rendering.Material;
import core.rendering.Mesh;
import core.rendering.Model;
import core.rendering.Shader;
import gmaths.*;
import static core.constants.Constants.*;

public class Room {

  private Model[] walls;
  private Window window;

  private Camera camera;
  private Light light1;
  private Light light2;
  private Spotlight spotlight1;

  private Texture texture_wall, texture_window_ground, texture_window_sky;
  private Shader wall_shader, window_sky_shader, window_ground_shader;

  public Room(GL3 gl, Camera c, Light light1, Light light2, Spotlight spotlight1, Shader wall_shader, Shader window_shader, Shader window_ground_shader, Texture texture_wall, Texture texture_window_ground, Texture texture_window_sky) {
    this.camera = c;
    this.light1 = light1;
    this.light2 = light2;
    this.spotlight1 = spotlight1;

    this.texture_wall = texture_wall;
    this.texture_window_ground = texture_window_ground;
    this.texture_window_sky = texture_window_sky;

    this.wall_shader = wall_shader;
    this.window_sky_shader = window_shader;
    this.window_ground_shader = window_ground_shader;

    walls = new Model[4];
    walls[0] = makeFloor(gl);
    walls[1] = makeWall1(gl);
    walls[2] = makeWall2(gl);
    window = new Window(makeWindow(gl), makeGroundWindow(gl));
  }
 
  private Model makeFloor(GL3 gl) {
    // grey basecolor with main colour given by texture map
    // Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    // Material material = new Material(new Vec3(0.5f, 0.5f, 0.5f), basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Material material = new Material();
    //create floor
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(WALL_SIZE,1f,WALL_SIZE), modelMatrix);
    Mesh mesh = new Mesh(gl, Surface.VERTICES.clone(), Surface.INDICES.clone());
    Shader shader = wall_shader;
    Model model = new Model(gl, camera, light1, light2, spotlight1, shader, material, modelMatrix, mesh, texture_wall);
    return model;
  }
 // right wall
  private Model makeWall1(GL3 gl) {
    // grey basecolor with main colour given by texture map
    // Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(WALL_SIZE,1f,WALL_SIZE), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(WALL_SIZE*0.5f,WALL_SIZE*0.5f,0), modelMatrix);

    Mesh mesh = new Mesh(gl, Surface.VERTICES.clone(), Surface.INDICES.clone());
    Shader shader = wall_shader;
    Model model = new Model(gl, camera, light1, light2, spotlight1, shader , material, modelMatrix, mesh, texture_wall);
    return model;
  }

  // left wall
  private Model makeWall2(GL3 gl) {
    // Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    // Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Material material = new Material();
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(WALL_SIZE,1f,WALL_SIZE), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-WALL_SIZE*0.5f,WALL_SIZE*0.5f,0), modelMatrix);
    Mesh mesh = new Mesh(gl, Surface.VERTICES.clone(), Surface.INDICES.clone());
    Shader shader = wall_shader;
    // no texture on this model
    Model model = new Model(gl, camera, light1, light2, spotlight1, shader, material, modelMatrix, mesh, texture_wall);
    return model;
  }

  private Model makeWindow(GL3 gl){
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mesh mesh = new Mesh(gl, Surface.VERTICES.clone(), Surface.INDICES.clone());
    Model model = new Model(gl, camera, light1, light2, spotlight1, window_sky_shader, material, new Mat4(), mesh, texture_window_sky);
    return model;
  }
  private Model makeGroundWindow(GL3 gl){
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mesh mesh = new Mesh(gl, Surface.VERTICES.clone(), Surface.INDICES.clone());
    Model model = new Model(gl, camera, light1, light2, spotlight1, window_ground_shader, material, new Mat4(), mesh, texture_window_ground);
    return model;
  }

  public Window getWindow(){
    return window;
  }

  public void render(GL3 gl) {
    for (int i=0; i<3; i++) {
       walls[i].render(gl);
    }
    window.render(gl);
    //test
    // wall[2].render(gl);
  }

  public void dispose(GL3 gl) {
    for (int i=0; i<3; i++) {
      walls[i].dispose(gl);
    }
    window.dispose(gl);
  }
}
