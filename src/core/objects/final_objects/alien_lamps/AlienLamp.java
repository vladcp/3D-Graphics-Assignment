package core.objects.final_objects.alien_lamps;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import core.camera.Camera;
import core.lights.Light;
import core.lights.Spotlight;
import core.nodes.NameNode;
import core.nodes.TransformNode;
import core.rendering.Material;
import core.rendering.Mesh;
import core.rendering.Model;
import core.rendering.Shader;
import gmaths.*;
import static core.constants.Constants.*;

/**
 * AlienLamp class
 * responsible for creating an animated articulated structure
 * resembling an extraterrestrial lamp
 * 
 * @author Vlad Prisacariu
 * November 2022
 */
public class AlienLamp {
  private NameNode root;

  // main parts
  private LampComponent base;
  private LampComponent lowerArm;
  private LampComponent sphereJoint;
  private LampComponent upperArm;
  private LampComponent lampHead;
  private LampComponent lampSpotlight;

  //decorations
  private LampComponent tail;
  private LampComponent eyeLeft;
  private LampComponent eyeRight;
  private LampComponent earLeft;
  private LampComponent earRight;

  // Transorm Nodes for the whole lamp
  // these set the different intial positions of the two lamps
  private TransformNode translateToPosition;
  private TransformNode rotateLamp;

  // front vector of the head lamp
  // used to set the spotlight direction
  private Vec3 frontHeadVector = new Vec3(1, 0, 0);
  private int dirAdjust = 1; //differs for the two lamps

  //animation variables
  private boolean isAnimating;
  
  private float startRotationLowerArm = LAMP1_POS1_LOWER_ARM, startRotationJoint = LAMP1_POS1_JOINT, startRotationHead = LAMP1_POS1_HEAD;
  private float frames = 0f, maxFrames = 90f; // animation duration

  // CURRENT ANIMATION VARIABLES
  private float activeEndRotationLowerArm, activeEndRotationJoint, activeEndRotationHead;
  private float currentRotationLowerArm = startRotationLowerArm, currentRotationJoint = startRotationJoint, currentRotationHead = startRotationHead;

  public AlienLamp(GL3 gl, Vec3 position, float rotationAngle, Camera camera, Light light1, Light light2, Spotlight spotlight1, 
    Spotlight spotlight2, Mesh cubeMesh, Mesh sphereMesh, Texture texture, Shader shader, String lampName){

    this.rotateLamp = new TransformNode("Rotate ALL ("+ rotationAngle + ")", new Mat4(Mat4Transform.rotateAroundY(rotationAngle)));
    this.translateToPosition = new TransformNode("Place at position + (" + position + ")", new Mat4(Mat4Transform.translate(position)));
    
    root = new NameNode(lampName);
    
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey

    //TODO ALL materials
    Material temporaryMaterial = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 0.2f);

    //TODO all lamp models
    Model baseModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), cubeMesh, texture);
    Model lowerArmModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), sphereMesh, texture);

    //TODO all lamp parts
    base = new LampComponent(LampComponentName.BASE, baseModel);
    lowerArm = new LampComponent(LampComponentName.LOWER_ARM, lowerArmModel);
    sphereJoint = new LampComponent(LampComponentName.SPHERE_JOINT, lowerArmModel);
    upperArm = new LampComponent(LampComponentName.UPPER_ARM, lowerArmModel);
    lampHead = new LampComponent(LampComponentName.HEAD, baseModel);
    earLeft = new LampComponent(LampComponentName.EAR_LEFT, lowerArmModel);
    earRight = new LampComponent(LampComponentName.EAR_RIGHT, lowerArmModel);

    switch(lampName) {
      //adjust appearance as well
      case "AlienLamp1":
        lampSpotlight = new LampComponent(LampComponentName.SPOTLIGHT, baseModel, position, spotlight1);
      break;
      case "AlienLamp2":
        dirAdjust = -1;
        lampSpotlight = new LampComponent(LampComponentName.SPOTLIGHT, baseModel, position, spotlight2);
      break;
    }
    
    tail = new LampComponent(LampComponentName.TAIL, baseModel);
    eyeLeft = new LampComponent(LampComponentName.EYE_LEFT, lowerArmModel);
    eyeRight = new LampComponent(LampComponentName.EYE_RIGHT, lowerArmModel);

    initialiseTransformNodes();
    initialiseRotateNodes();

    makeSceneGraph();

    this.isAnimating = false;
    // root.print(2, false);
  }

  public double getCurrentSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  public void draw(GL3 gl){
    // System.out.println("Current angle " + currentRotation);
    if (isAnimating) {
      animate();
    }
    root.draw(gl);
    // printAllModel();
    updateSpotlightPosition();
  }

  private TransformNode translateByHeight(float height, String name){
    TransformNode translateByHeight = new TransformNode(name, 
      new Mat4(Mat4Transform.translate(0f, height, 0f)));
    return translateByHeight;
  }
  // Translate Nodes (for stacking the components)
  private TransformNode translateLowerArm;
  private TransformNode translateJoint;
  private TransformNode translateUpperArm;
  private TransformNode translateLampHead;
  private TransformNode translateSpotlight;
  private TransformNode translateTail;
  private TransformNode translateLeftEye;
  private TransformNode translateRightEye;
  private TransformNode translateLeftEar;
  private TransformNode translateRightEar;

  private void initialiseTransformNodes(){
    translateLowerArm = translateByHeight(LAMP_BASE_HEIGHT + LAMP_ARM_SIZE/2f, "Translate Onto Base");
    translateJoint = translateByHeight(LAMP_ARM_SIZE/2f + LAMP_JOINT_SIZE/2f, "Translate Onto Lower Arm");
    translateUpperArm = translateByHeight(LAMP_JOINT_SIZE/2f + LAMP_ARM_SIZE/2f, "Translate Onto Joint");
    translateLampHead = translateByHeight(LAMP_ARM_SIZE/2f + LAMP_HEAD_HEIGHT/2f, "Translate Onto Upper Arm");

    translateSpotlight = new TransformNode("Translate Onto End of Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT + LAMP_SPOTLIGHT_LENGTH/2f,
      0f, 0f)));
    translateTail = new TransformNode("Translate behind the Joint", new Mat4(Mat4Transform.translate(-(LAMP_JOINT_SIZE/2f + LAMP_TAIL_LENGTH/2f), 0f, 0f)));

    translateLeftEye = new TransformNode("Translate Left Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
    translateRightEye = new TransformNode("Translate Right Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    
    translateLeftEar = new TransformNode("Translate Left Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    translateRightEar = new TransformNode("Translate Right Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
  
  }

  // Rotate nodes at each joint
  private TransformNode rotateLowerArm;
  private TransformNode rotateJoint;
  private TransformNode rotateHead;
  
  private void initialiseRotateNodes() {
    rotateLowerArm = new TransformNode("Rotate Lower Arm", Mat4Transform.rotateAroundY(startRotationLowerArm));
    rotateJoint = new TransformNode("Rotate Joint", Mat4Transform.rotateAroundZ(startRotationJoint));
    rotateHead = new TransformNode("Rotate Head", Mat4Transform.rotateAroundZ(startRotationHead));

    // update spotlight direction
    updateSpotlightDirection(startRotationLowerArm, startRotationJoint);
    updateSpotlightPosition();
  }

  public void initialiseAnimation(int animation) {
    this.isAnimating = true;
    //reset startAnglePositions
    setStartRotations();
    frames = 0;
    switch(animation) {
      case 1:
        // first pos of first lamp
        activeEndRotationLowerArm = LAMP1_POS1_LOWER_ARM;
        activeEndRotationJoint = LAMP1_POS1_JOINT;
        activeEndRotationHead = LAMP1_POS1_HEAD;
      break;
      case 2:
        // second pos of first lamp
        activeEndRotationLowerArm = LAMP1_POS2_LOWER_ARM;
        activeEndRotationJoint = LAMP1_POS2_JOINT;
        activeEndRotationHead = LAMP1_POS2_HEAD;
      break;
      case 3:
        // third pos of first lamp
        activeEndRotationLowerArm = LAMP1_POS3_LOWER_ARM;
        activeEndRotationJoint = LAMP1_POS3_JOINT;
        activeEndRotationHead = LAMP1_POS3_HEAD;
      break;
      case 4:
        activeEndRotationLowerArm = LAMP2_POS1_LOWER_ARM;
        activeEndRotationJoint = LAMP2_POS1_JOINT;
        activeEndRotationHead = LAMP2_POS1_HEAD;
      break;
      case 5:
        activeEndRotationLowerArm = LAMP2_POS2_LOWER_ARM;
        activeEndRotationJoint = LAMP2_POS2_JOINT;
        activeEndRotationHead = LAMP2_POS2_HEAD;
      break;
      case 6:
        activeEndRotationLowerArm = LAMP2_POS3_LOWER_ARM;
        activeEndRotationJoint = LAMP2_POS3_JOINT;
        activeEndRotationHead = LAMP2_POS3_HEAD;
      break;
    }
  }

  private void setStartRotations() {
    startRotationLowerArm = currentRotationLowerArm;
    startRotationJoint = currentRotationJoint;
    startRotationHead = currentRotationHead;
  }

  public void animate() {
    if (frames > maxFrames) {
      isAnimating = false;
      frames = 0;
      
      startRotationLowerArm = activeEndRotationLowerArm;
      startRotationJoint = activeEndRotationJoint;
      startRotationHead = activeEndRotationHead;

      return;
    }
    float fr = frames/maxFrames;
    currentRotationLowerArm = lerp(startRotationLowerArm, activeEndRotationLowerArm, fr);
    currentRotationJoint = lerp(startRotationJoint, activeEndRotationJoint, fr);
    currentRotationHead = lerp(startRotationHead, activeEndRotationHead, fr);

    rotateLowerArm.setTransform(Mat4Transform.rotateAroundY(currentRotationLowerArm));
    rotateJoint.setTransform(Mat4Transform.rotateAroundZ(currentRotationJoint));
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(currentRotationHead));

    updateSpotlightDirection(currentRotationLowerArm, currentRotationJoint + currentRotationHead);
    root.update();
    updateSpotlightPosition();
    frames ++;
  }

  private void updateSpotlightDirection(float yRotation, float zRotation) {
    updateHeadFrontVector(0f, -dirAdjust * (float)Math.toRadians(yRotation), (float)Math.toRadians(zRotation));
    lampSpotlight.getSpotlightNode().setSpotlightDirection(frontHeadVector);
  }

  private void updateSpotlightPosition() {
    lampSpotlight.getSpotlightNode().setSpotlightPosition(lampSpotlight.getSpotlightNode().getWorldTransform().getLastColumn());
  }
  public void setIsAnimating(boolean value) {
    this.isAnimating = value;
  }

  private float lerp(float min, float max, float fraction) {
    return (max - min) * fraction + min;
  }
  
  private void updateHeadFrontVector(float xRotation, float yRotation, float zRotation) {
    double cy, cp, sy, sp;
    cy = Math.cos(yRotation);
    sy = Math.sin(yRotation);
    cp = Math.cos(zRotation);
    sp = Math.sin(zRotation);
    frontHeadVector.x = dirAdjust * (float)(cy*cp);
    frontHeadVector.y = (float)(sp);
    frontHeadVector.z = (float)(sy*cp);
    frontHeadVector.normalize();
  } 

  private void makeSceneGraph(){
    root.addChild(translateToPosition); // translates to -3, 0, 0
    translateToPosition.addChild(rotateLamp);
      rotateLamp.addChild(base.getNameNode()); // add base node
        base.getNameNode().addChild(rotateLowerArm);
          rotateLowerArm.addChild(translateLowerArm);
            translateLowerArm.addChild(lowerArm.getNameNode());
              lowerArm.getNameNode().addChild(translateJoint);
                translateJoint.addChild(rotateJoint);
                  rotateJoint.addChild(sphereJoint.getNameNode());
                    sphereJoint.getNameNode().addChild(translateUpperArm);
                    sphereJoint.getNameNode().addChild(translateTail);
                      translateTail.addChild(tail.getNameNode());
                      translateUpperArm.addChild(upperArm.getNameNode());
                        upperArm.getNameNode().addChild(translateLampHead);
                          translateLampHead.addChild(rotateHead);
                            rotateHead.addChild(lampHead.getNameNode());
                              lampHead.getNameNode().addChild(translateSpotlight);
                              lampHead.getNameNode().addChild(translateLeftEye);
                              lampHead.getNameNode().addChild(translateRightEye);
                              lampHead.getNameNode().addChild(translateLeftEar);
                              lampHead.getNameNode().addChild(translateRightEar);
                                translateRightEar.addChild(earRight.getNameNode());
                                translateLeftEar.addChild(earLeft.getNameNode());
                                translateRightEye.addChild(eyeRight.getNameNode());
                                translateLeftEye.addChild(eyeLeft.getNameNode());
                                translateSpotlight.addChild(lampSpotlight.getNameNode());
    root.update();
  }
}
