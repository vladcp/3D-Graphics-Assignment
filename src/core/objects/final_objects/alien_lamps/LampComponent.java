package core.objects.final_objects.alien_lamps;
// this class will store all model matrices for all lamp parts

import core.nodes.ModelNode;
import core.nodes.NameNode;
import core.nodes.SpotlightNode;
import core.nodes.TransformNode;
import core.rendering.Model;
import gmaths.*;

import static core.Constants.*;

import core.lights.Spotlight;

/**
 * Helper class for building an {@link AlienLamp}
 * It essentially provides the leaf nodes in the alien lamp scene graph
 * i.e. the name, model and scale nodes for each primitive part of the lamp
 * @author Vlad Prisacariu
 */
public class LampComponent {
  public enum LampComponentName {
    BASE("Base"),
    LOWER_ARM("Lower_Arm"),
    SPHERE_JOINT("Sphere Joint"),// can attach TAIL
    UPPER_ARM("Upper Arm"),
    HEAD("Head"), //has SPOTLIGHT, has decorations
    SPOTLIGHT("Spotlight"),
  
    TAIL("Tail"),
    EAR_LEFT("Ear Left"),
    EAR_RIGHT("Ear Right"),
    EYE_LEFT("Eye Left"),
    EYE_RIGHT("Eye Right"),
  
    CHIN("Chin"),
    LIMB("Limb");
    
    public final String NAME;
    // constructor
    private LampComponentName(String name){
      NAME = name;
    }
  }
  
  private NameNode nameNode;
  private ModelNode modelNode;
  private SpotlightNode spotlightNode;
  private TransformNode scaleNode;

  //position is the center of the base where the lamp is placed.
  public LampComponent(LampComponentName componentName, Model model, Vec3 position, Spotlight spotlight) {
    switch(componentName) {
      case BASE:
        constructor("Base Scale", baseScaleTransform(), "Base model", model, "Base");
        break;
      case LOWER_ARM:
        constructor("Lower Arm Scale", lowerArmScaleTransform(), "Lower Arm Model", model, "Lower Arm");
        break;
      case SPHERE_JOINT:
        constructor("Sphere Joint Scale", jointScaleTransform(), "Sphere Joint Model", model, "Sphere Joint");
      break;
      case UPPER_ARM:
        constructor("Upper Arm Scale", upperArmScaleTransform(), "Upper Arm Model", model, "Upper Arm");
      break;
      case HEAD:
        constructor("Head Scale", headScaleTransform(), "Head Model", model, "Head");
      break;
      case SPOTLIGHT:
        constructor("Spotlight Scale", spotlightScaleTransform(position, spotlight), "Spotlight Model", spotlight, "Spotlight");
      break;
      case TAIL:
        constructor("Tail Scale", tailScaleTransform(), "Tail model", model, "Tail");
      break;
      case EYE_LEFT:
        constructor("Eye Left Scale", eyeScaleTransform(), "Eye Left Model", model, "Eye Left");
      break;
      case EYE_RIGHT:
        constructor("Eye Right Scale", eyeScaleTransform(), "Eye Right Model", model, "Eye Right");
      break;
      case EAR_LEFT:
        constructor("Ear left Scale", earScaleTransform(), "Ear Left Model", model, "Eye Left");
      break;
      case EAR_RIGHT:
        constructor("Ear Right Scale", earScaleTransform(), "Ear Right Model", model, "Eye Right");
      break;
      case CHIN:
        constructor("Chin Scale", chinScaleTransform(), "Chin Model", model, "Chin");
      break;
      case LIMB:
        constructor("Limb Scale", limbScaleTransform(), "Limb Model", model, "Limb");
      break;
    }
    makeSceneGraphPart();
    
  }

  public LampComponent(LampComponentName componentName, Model model) {
    this(componentName, model, null, null);
  }

  private Mat4 baseScaleTransform(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_BASE_HEIGHT*3f, LAMP_BASE_HEIGHT, LAMP_BASE_HEIGHT*2f), new Mat4(1));
    matrix = Mat4.multiply(Mat4Transform.translate(new Vec3(0, LAMP_BASE_HEIGHT/2f, 0)), matrix);
    return matrix;
  }

  private Mat4 lowerArmScaleTransform(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_ARM_WIDTH, LAMP_ARM_LENGTH, LAMP_ARM_WIDTH), new Mat4(1));
    return matrix;
  }

  private Mat4 jointScaleTransform(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(0.3f, 0.3f, 0.3f), new Mat4(1));
    return matrix;
  }

  private Mat4 upperArmScaleTransform(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_ARM_WIDTH, LAMP_ARM_LENGTH, LAMP_ARM_WIDTH), new Mat4(1));
    return matrix;
  }

  private Mat4 headScaleTransform(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_HEAD_HEIGHT*2, LAMP_HEAD_HEIGHT, LAMP_HEAD_HEIGHT), new Mat4(1));
    return matrix;
  }

  public Mat4 spotlightScaleTransform(Vec3 position, Spotlight spotlight){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_SPOTLIGHT_LENGTH, LAMP_SPOTLIGHT_LENGTH, LAMP_SPOTLIGHT_LENGTH), new Mat4(1));
    return matrix;
  }

  public Mat4 tailScaleTransform() {
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_TAIL_LENGTH, LAMP_TAIL_LENGTH/7f, LAMP_TAIL_LENGTH/7f), new Mat4(1));
    return matrix;
  }

  public Mat4 eyeScaleTransform() {
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_EYE_SIZE, LAMP_EYE_SIZE, LAMP_EYE_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 earScaleTransform() {
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(LAMP_EYE_SIZE, LAMP_EAR_LENGTH, LAMP_EYE_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 chinScaleTransform() {
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(LAMP_CHIN_SIZE, LAMP_CHIN_SIZE*3f, LAMP_CHIN_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 limbScaleTransform() {
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(LAMP_LIMP_WIDTH, LAMP_LIMB_LENGTH, LAMP_LIMP_WIDTH), new Mat4(1));
    return matrix;
  }
  // for non spotlight
  private void constructor(String transformName, Mat4 modelMatrix, String modelName, Model model, String nodeName){
    nameNode = new NameNode(nodeName);
    scaleNode = new TransformNode(transformName, modelMatrix);
    modelNode = new ModelNode(modelName, model);
  }

  // for spotlight
  private void constructor(String transformName, Mat4 modelMatrix, String modelName, Spotlight spotlight, String nodeName){
    nameNode = new NameNode(nodeName);
    scaleNode = new TransformNode(transformName, modelMatrix);
    spotlightNode = new SpotlightNode(modelName, spotlight);
  }

  /**
   * Make the scene graph part containing the model node and scale node
   * of this component. This makes the scene graph in {@link AlienLamp}
   * more concise, as it is a repeated part for all components
   */
  private void makeSceneGraphPart(){
    nameNode.addChild(scaleNode);
    if(this.spotlightNode != null){
      scaleNode.addChild(spotlightNode);
    } else {
      scaleNode.addChild(modelNode);
    }
  }

  public NameNode getNameNode(){
    return nameNode;
  }

  public TransformNode getTransformNode(){
    return scaleNode;
  }
  
  public ModelNode getModelNode(){
    return modelNode;
  }
  
  public SpotlightNode getSpotlightNode() {
    return spotlightNode;
  }
}
