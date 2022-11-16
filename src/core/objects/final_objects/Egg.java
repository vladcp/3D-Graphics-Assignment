package core.objects.final_objects;

import core.rendering.Model;
import gmaths.*;
import static core.constants.Constants.*;

import com.jogamp.opengl.GL3;
public class Egg {

  private Model eggModel;
  private Model baseModel;

  private Mat4 eggModelMatrix;
  private Mat4 baseModelMatrix;


  public Egg(Model eggMod, Model baseMod){
    eggModel = eggMod;
    baseModel = baseMod;

    eggModelMatrix = getEggModelMatrix();
    baseModelMatrix = getBaseModelMatrix();
  }

  public Mat4 getEggModelMatrix(){
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(EGG_WIDTH, EGG_HEIGHT, EGG_WIDTH), new Mat4(1f));
    matrix = Mat4.multiply(Mat4Transform.translate(0f, TABLE_HEIGHT + EGG_BASE_HEIGHT + EGG_HEIGHT/2f, 0f), matrix);

    return matrix;
  }

  public Mat4 getBaseModelMatrix(){
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(TABLE_LENGTH/2f, EGG_BASE_HEIGHT, TABLE_LENGTH/2f), new Mat4(1f));
    matrix = Mat4.multiply(Mat4Transform.translate(0f, 0.25f + TABLE_HEIGHT + EGG_BASE_HEIGHT/2f, 0f), matrix);

    return matrix;
  }

  public void render(GL3 gl) {
    baseModel.setModelMatrix(baseModelMatrix);
    eggModel.setModelMatrix(eggModelMatrix);

    baseModel.render(gl);
    eggModel.render(gl);
  }

  public void dispose(GL3 gl) {
    eggModel.dispose(gl);
    baseModel.dispose(gl);
  }
}
