package core.objects.final_objects.alien_lamps;
// this class will store all model matrices for all lamp parts

import core.nodes.ModelNode;
import core.nodes.NameNode;
import core.nodes.SpotlightNode;
import core.nodes.TransformNode;
import core.rendering.Model;
import gmaths.*;
import static core.constants.Constants.*;

import core.lights.Spotlight;

/**
 * Helper class for building an {@link AlienLamp}
 * It essentially provides the leaf nodes in the alien lamp scene graph
 * i.e. the name, model and scale nodes for each primitive part of the lamp
 * @author Vlad Prisacariu
 */
public class LampComponent {
  // namely: BASE, LOWER BRANCH, SPHERE JOINT, UPPER ARM, LOWER ARM, HEAD
  // note: these components might have decorations that need their own model matrices
  private NameNode nameNode;
  private ModelNode modelNode;
  private SpotlightNode spotlightNode;
  private TransformNode scaleNode;

  //position is the center of the base where the lamp is placed.
  public LampComponent(LampComponentName componentName, Model model, Vec3 position, Spotlight spotlight) {
    switch(componentName) {
      case BASE:
        constructor("Base Scale", baseMatrix(), "Base model", model, "Base");
        break;
      case LOWER_ARM:
        constructor("Lower Arm Scale", lowerArmMatrix(), "Lower Arm Model", model, "Lower Arm");
        break;
      case SPHERE_JOINT:
        constructor("Sphere Joint Scale", sphereJointMatrix(), "Sphere Joint Model", model, "Sphere Joint");
      break;
      case UPPER_ARM:
        constructor("Upper Arm Scale", upperArmMatrix(), "Upper Arm Model", model, "Upper Arm");
      break;
      case HEAD:
        constructor("Head Scale", lampHeadMatrix(), "Head Model", model, "Head");
      break;
      case SPOTLIGHT:
        constructor("Spotlight Scale", spotlightMatrix(position, spotlight), "Spotlight Model", spotlight, "Spotlight");
      break;
      case TAIL:
        constructor("Tail Scale", tailMatrix(), "Tail model", model, "Tail");
      break;
      case EYE_LEFT:
        constructor("Eye Left Scale", eyeLeftMatrix(), "Eye Left Model", model, "Eye Left");
      break;
      case EYE_RIGHT:
        constructor("Eye Right Scale", eyeLeftMatrix(), "Eye Right Model", model, "Eye Right");
      break;
      case EAR_LEFT:
        constructor("Ear left Scale", earMatrix(), "Ear Left Model", model, "Eye Left");
      break;
      case EAR_RIGHT:
        constructor("Ear Right Scale", earMatrix(), "Ear Right Model", model, "Eye Right");
      break;
      case CHIN:
        constructor("Chin Scale", chinMatrix(), "Chin Model", model, "Chin");
      break;
      case LIMB:
        constructor("Limb Scale", limbMatrix(), "Limb Model", model, "Limb");
      break;
      default:
        System.err.println("Lamp component name = " + componentName + " does not exist");
    }
    makeSceneGraphPart();
    
  }

  public LampComponent(LampComponentName componentName, Model model) {
    this(componentName, model, null, null);
  }

  private Mat4 baseMatrix(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_BASE_HEIGHT*3f, LAMP_BASE_HEIGHT, LAMP_BASE_HEIGHT*2f), new Mat4(1));
    matrix = Mat4.multiply(Mat4Transform.translate(new Vec3(0, LAMP_BASE_HEIGHT/2f, 0)), matrix);
    return matrix;
  }

  private Mat4 lowerArmMatrix(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_ARM_WIDTH, LAMP_ARM_LENGTH, LAMP_ARM_WIDTH), new Mat4(1));
    return matrix;
  }

  private Mat4 sphereJointMatrix(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(0.3f, 0.3f, 0.3f), new Mat4(1));
    return matrix;
  }

  private Mat4 upperArmMatrix(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_ARM_WIDTH, LAMP_ARM_LENGTH, LAMP_ARM_WIDTH), new Mat4(1));
    return matrix;
  }

  private Mat4 lampHeadMatrix(){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_HEAD_HEIGHT*2, LAMP_HEAD_HEIGHT, LAMP_HEAD_HEIGHT), new Mat4(1));
    return matrix;
  }

  public Mat4 spotlightMatrix(Vec3 position, Spotlight spotlight){
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_SPOTLIGHT_LENGTH, LAMP_SPOTLIGHT_LENGTH, LAMP_SPOTLIGHT_LENGTH), new Mat4(1));
    return matrix;
  }

  public Mat4 tailMatrix() {
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_TAIL_LENGTH, LAMP_TAIL_LENGTH/7f, LAMP_TAIL_LENGTH/7f), new Mat4(1));
    return matrix;
  }

  public Mat4 eyeLeftMatrix() {
    Mat4 matrix = Mat4.multiply(
      Mat4Transform.scale(LAMP_EYE_SIZE, LAMP_EYE_SIZE, LAMP_EYE_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 earMatrix() {
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(LAMP_EYE_SIZE, LAMP_EAR_LENGTH, LAMP_EYE_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 chinMatrix() {
    Mat4 matrix = Mat4.multiply(Mat4Transform.scale(LAMP_CHIN_SIZE, LAMP_CHIN_SIZE*3f, LAMP_CHIN_SIZE), new Mat4(1));
    return matrix;
  }

  public Mat4 limbMatrix() {
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

  // the part of the scene graph that makes this component
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
