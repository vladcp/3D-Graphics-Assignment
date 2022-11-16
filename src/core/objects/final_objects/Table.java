package core.objects.final_objects;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import core.camera.Camera;
import core.lights.Light;
import core.lights.Spotlight;
import core.objects.base.Cube;
import core.rendering.Material;
import core.rendering.Mesh;
import core.rendering.Model;
import core.rendering.Shader;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import static core.constants.Constants.*;
public class Table {
  private Model leg1, leg2, leg3, leg4, top;
  private Camera camera;
  private Light light1;
  private Light light2;
  private Spotlight spotlight1;

  private Texture texture;
  private Shader shader;

  // Build a square table centered at origin
  public Table(GL3 gl, Camera c, Light light1, Light light2, Spotlight spotlight1, Shader shader, Texture texture) {
    this.camera = c;
    this.light1 = light1;
    this.light2 = light2;
    this.texture = texture;
    this.shader = shader;
    this.spotlight1 = spotlight1;

    makeTable(gl, TABLE_LENGTH, TABLE_HEIGHT);
  }

  private void makeTable(GL3 gl, float tableLength, float tableHeight) {
    Mesh m = new Mesh(gl, Cube.VERTICES.clone(), Cube.INDICES.clone());
    Material mat = new Material();
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(.25f, tableHeight, .25f), new Mat4(1));

    modelMatrix = Mat4.multiply(Mat4Transform.translate(-tableLength/2, tableHeight/2, tableLength/2), modelMatrix);
    leg1 = new Model(gl, camera, light1, light2, spotlight1, shader, mat, modelMatrix, m, texture);

    modelMatrix = Mat4.multiply(Mat4Transform.translate(tableLength, 0, 0), modelMatrix);
    leg2 = new Model(gl, camera, light1, light2, spotlight1, shader, mat, modelMatrix, m);

    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, 0, -tableLength), modelMatrix);
    leg3 = new Model(gl, camera, light1, light2, spotlight1, shader, mat, modelMatrix, m);

    modelMatrix = Mat4.multiply(Mat4Transform.translate(-tableLength, 0, 0), modelMatrix);
    leg4 = new Model(gl, camera, light1, light2, spotlight1, shader, mat, modelMatrix, m);

    modelMatrix = Mat4.multiply(Mat4Transform.scale(tableLength + .5f, .5f, tableLength+.5f), new Mat4(1));
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, tableHeight, 0), modelMatrix);
    top = new Model(gl, camera, light1, light2, spotlight1, shader, mat, modelMatrix, m);
  }

  public void render(GL3 gl){
    leg1.render(gl);
    leg2.render(gl);
    leg3.render(gl);
    leg4.render(gl);
    top.render(gl);
    // System.out.println("Table New position: " + top.getModelMatrix().getLastColumn());
  }


  public void dispose(GL3 gl){
    leg1.dispose(gl);
    leg2.dispose(gl);
    leg3.dispose(gl);
    leg4.dispose(gl);
    top.dispose(gl);
  }

}
