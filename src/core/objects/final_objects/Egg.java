package core.objects.final_objects;

import core.nodes.*;
import core.rendering.Model;
import gmaths.*;
import com.jogamp.opengl.GL3;
import static core.constants.Constants.*;
/**
 * Egg Class
 * creates an animated egg and the stand on which it sits
 * @author Vlad Prisacariu,
 * November 2022
 */

public class Egg {

  private Model eggModel;
  private Model baseModel;
  
  private NameNode root;
  
  private TransformNode rotateEggZ;

  private float rotateEggAngle;
  private float jumpDistance;

  private TransformNode translateEggY;
  // rotation Nodes
  public Egg(Model eggMod, Model baseMod){
    root = new NameNode("Egg&Stand");

    eggModel = eggMod;
    baseModel = baseMod;


    makeSceneGraph();
  }

  public Mat4 getEggModelMatrix(){
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(EGG_WIDTH, EGG_HEIGHT, EGG_WIDTH), new Mat4(1f));
    // matrix = Mat4.multiply(Mat4Transform.translate(0f, EGG_HEIGHT/2f, 0f), matrix);
    return matrix;
  }

  public Mat4 getBaseModelMatrix(){
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(TABLE_LENGTH/2f, EGG_BASE_HEIGHT, TABLE_LENGTH/2f), new Mat4(1f));
    matrix = Mat4.multiply(Mat4Transform.translate(0f, EGG_BASE_HEIGHT/2f, 0f), matrix);
    return matrix;
  }

  public void animate(double elapsedTime) {
    rotateEggAngle = 8f * (float)Math.sin(elapsedTime*5);
    rotateEggZ.setTransform(Mat4Transform.rotateAroundZ(rotateEggAngle));

    jumpDistance  = Math.abs((float) Math.sin(elapsedTime * 2)) / 2f;
    translateEggY.setTransform(Mat4Transform.translate(0f, jumpDistance, 0f));
    root.update();
  }
//TODO make scene graph for table and egg
  private void makeSceneGraph() {
    NameNode egg = new NameNode("Egg Node");
    ModelNode eggModelNode = new ModelNode("eggModelNode", eggModel);
    TransformNode eggScaleNode = new TransformNode("Egg Scale", getEggModelMatrix());
    
    NameNode base = new NameNode("Base Node");
    ModelNode baseModelNode = new ModelNode("Base Model Node", baseModel);
    TransformNode baseScaleNode = new TransformNode("Base Scale Node", getBaseModelMatrix());
    
    TransformNode translateEggOntoBase = new TransformNode("translate egg on stand", 
      Mat4Transform.translate(0f, EGG_BASE_HEIGHT + EGG_HEIGHT/2f, 0f));
    TransformNode translateOntoTable = new TransformNode("Translate on table", 
      Mat4Transform.translate(0f, TABLE_HEIGHT + .25f, 0f));
      
    rotateEggZ = new TransformNode("Rotate Egg", 
      Mat4Transform.rotateAroundZ(30f));
    translateEggY = new TransformNode("Jump Egg", 
      Mat4Transform.translate(0f, .5f, 0f));

    root.addChild(translateOntoTable);
    translateOntoTable.addChild(base);
      base.addChild(baseScaleNode);
        baseScaleNode.addChild(baseModelNode);
      base.addChild(rotateEggZ);
        rotateEggZ.addChild(translateEggOntoBase);
        translateEggOntoBase.addChild(translateEggY);
          translateEggY.addChild(egg);
            egg.addChild(eggScaleNode);
              eggScaleNode.addChild(eggModelNode);
    // add child stand etc
      // add child egg etc
      root.update();
  }
  public void render(GL3 gl) {
    root.draw(gl);
  }

  public void dispose(GL3 gl) {
    // root.dispose
  }
}
