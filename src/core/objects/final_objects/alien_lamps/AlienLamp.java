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
import core.rendering.TextureLibrary;
import gmaths.*;

import static core.constants.Constants.*;

import java.util.List;

/**
 * AlienLamp class
 * responsible for creating an animated articulated structure
 * resembling an extraterrestrial lamp
 * 
 * @author Vlad Prisacariu 
 * vcprisacariu1@sheffield.ac.uk
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
  private LampComponent chin;
  private LampComponent limbLeft;
  private LampComponent limbRight;
  // Transform Nodes for the whole lamp
  // these set the different intial positions of the two lamps
  private TransformNode translateToPosition;
  private TransformNode rotateLamp;

  // front vector of the lamp head
  // used to set the spotlight direction
  private Vec3 frontHeadVector = new Vec3(1, 0, 0);
  private int spotlightDirAdjust = 1; //differs for the two lamps

  //animation variables
  private boolean isAnimating;
  private float frames = 0f, maxFrames = 90f; // animation duration
  
  // animation angles: start, end and current for interpolation
  private float startRotationLowerArmY, startRotationJoint, startRotationHead,
  startRotationLowerArmZ;
  private float activeEndRotationLowerArmY, activeEndRotationLowerArmZ, activeEndRotationJoint, activeEndRotationHead;
  private float currentRotationLowerArmY, currentRotationJoint, currentRotationHead,
  currentRotationLowerArmZ;

  private float rotationEar = -25f;


  public AlienLamp(GL3 gl, Camera camera, Light light1, Light light2, Spotlight spotlight1, 
    Spotlight spotlight2, Mesh cubeMesh, Mesh sphereMesh, List<Texture> textureList, Shader shader, String lampName){
    
    root = new NameNode(lampName);
    
    Vec3 basecolor = new Vec3(0.2f, 0.2f, 0.2f); // grey

    //TODO ALL materials
    Material temporaryMaterial = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 0.2f);

    //TODO all lamp models
    
    //TODO all lamp parts
    Model baseModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), cubeMesh, textureList.get(TextureLibrary.DROPLETS));
    Model lowerArmModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), sphereMesh, textureList.get(TextureLibrary.PLANET));  
    
    switch(lampName) {
      case "AlienLamp1":
        baseModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), cubeMesh, textureList.get(TextureLibrary.DROPLETS));
        lowerArmModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), sphereMesh, textureList.get(TextureLibrary.SPOTTED));  
        
        base = new LampComponent(LampComponentName.BASE, baseModel);
        lowerArm = new LampComponent(LampComponentName.LOWER_ARM, lowerArmModel);
        sphereJoint = new LampComponent(LampComponentName.SPHERE_JOINT, lowerArmModel);
        upperArm = new LampComponent(LampComponentName.UPPER_ARM, lowerArmModel);
        lampHead = new LampComponent(LampComponentName.HEAD, baseModel);
        earLeft = new LampComponent(LampComponentName.EAR_LEFT, lowerArmModel);
        earRight = new LampComponent(LampComponentName.EAR_RIGHT, lowerArmModel);
        tail = new LampComponent(LampComponentName.TAIL, baseModel);
        eyeLeft = new LampComponent(LampComponentName.EYE_LEFT, lowerArmModel);
        eyeRight = new LampComponent(LampComponentName.EYE_RIGHT, lowerArmModel);
        chin = new LampComponent(LampComponentName.CHIN, lowerArmModel);
        
        lampSpotlight = new LampComponent(LampComponentName.SPOTLIGHT, baseModel, LAMP1_POSITION, spotlight1);

        this.rotateLamp = new TransformNode("Rotate ALL ("+ LAMP1_ROTATION + " degrees)", new Mat4(Mat4Transform.rotateAroundY(LAMP1_ROTATION)));
        this.translateToPosition = new TransformNode("Place at position + (" + LAMP1_POSITION + ")", new Mat4(Mat4Transform.translate(LAMP1_POSITION)));
        
        startRotationLowerArmY = LAMP1_POS1_LOWER_ARM_Y;
        startRotationLowerArmZ = LAMP1_POS1_LOWER_ARM_Z;
        startRotationHead = LAMP1_POS1_HEAD;
        startRotationJoint = LAMP1_POS1_JOINT;

        initialiseTranformNodes_L1();
        initialiseRotateNodes_L1();
        makeSceneGraph_L1();
      break;
      case "AlienLamp2":
        baseModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), cubeMesh, textureList.get(TextureLibrary.TABLE));
        lowerArmModel = new Model(gl, camera, light1, light2, spotlight1, spotlight2, shader, temporaryMaterial, new Mat4(1), sphereMesh, textureList.get(TextureLibrary.JAGUAR));

        base = new LampComponent(LampComponentName.BASE, baseModel);
        lowerArm = new LampComponent(LampComponentName.LOWER_ARM, lowerArmModel);
        sphereJoint = new LampComponent(LampComponentName.SPHERE_JOINT, lowerArmModel);
        upperArm = new LampComponent(LampComponentName.UPPER_ARM, lowerArmModel);
        lampHead = new LampComponent(LampComponentName.HEAD, baseModel);
        earLeft = new LampComponent(LampComponentName.EAR_LEFT, lowerArmModel);
        earRight = new LampComponent(LampComponentName.EAR_RIGHT, lowerArmModel);
        tail = new LampComponent(LampComponentName.TAIL, baseModel);
        eyeLeft = new LampComponent(LampComponentName.EYE_LEFT, lowerArmModel);
        eyeRight = new LampComponent(LampComponentName.EYE_RIGHT, lowerArmModel);

        lampSpotlight = new LampComponent(LampComponentName.SPOTLIGHT, baseModel, LAMP2_POSITION, spotlight2);
        
        this.rotateLamp = new TransformNode("Rotate ALL ("+ LAMP2_ROTATION + " degrees)", new Mat4(Mat4Transform.rotateAroundY(LAMP2_ROTATION)));
        this.translateToPosition = new TransformNode("Place at position + (" + LAMP2_POSITION + ")", new Mat4(Mat4Transform.translate(LAMP2_POSITION)));

        limbLeft = new LampComponent(LampComponentName.LIMB, lowerArmModel);
        limbRight = new LampComponent(LampComponentName.LIMB, lowerArmModel);

        startRotationLowerArmY = LAMP2_POS1_LOWER_ARM_Y;
        startRotationLowerArmZ = LAMP2_POS1_LOWER_ARM_Z;
        startRotationHead = LAMP2_POS1_HEAD;
        startRotationJoint = LAMP2_POS1_JOINT;

        spotlightDirAdjust = -1;

        initialiseTranformNodes_L2();
        initialiseRotateNodes_L2();
        makeSceneGraph_L2();
      break;
    }

    currentRotationHead = startRotationHead;
    currentRotationJoint = startRotationJoint;
    currentRotationLowerArmY = startRotationLowerArmY;
    currentRotationLowerArmZ = startRotationLowerArmZ;
    
    // update spotlight direction
    updateSpotlightDirection(startRotationLowerArmY, startRotationJoint + startRotationHead + startRotationLowerArmZ);
    updateSpotlightPosition();

    this.isAnimating = false;
    root.print(2, false);
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
    // updateSpotlightPosition();
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
  private TransformNode translateChin;

  // for lamp 2
  private TransformNode translateLimbLeft;
  private TransformNode translateLimbRight;

  private void initialiseTranformNodes_L1(){
    translateLowerArm = translateByHeight(LAMP_BASE_HEIGHT + LAMP_ARM_LENGTH/2f, "Translate Onto Base");
    translateJoint = translateByHeight(LAMP_ARM_LENGTH/2f + LAMP_JOINT_SIZE/2f, "Translate Onto Lower Arm");
    translateUpperArm = translateByHeight(LAMP_JOINT_SIZE/2f + LAMP_ARM_LENGTH/2f, "Translate Onto Joint");
    translateLampHead = translateByHeight(LAMP_ARM_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, "Translate Onto Upper Arm");

    translateSpotlight = new TransformNode("Translate Onto End of Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT + LAMP_SPOTLIGHT_LENGTH/2f,
      0f, 0f)));
    translateTail = new TransformNode("Translate behind the Joint", new Mat4(Mat4Transform.translate(-(LAMP_JOINT_SIZE/2f + LAMP_TAIL_LENGTH/2f), 0f, 0f)));

    translateLeftEye = new TransformNode("Translate Left Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
    translateRightEye = new TransformNode("Translate Right Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    
    translateLeftEar = new TransformNode("Translate Left Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    translateRightEar = new TransformNode("Translate Right Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
    translateChin = new TransformNode("Translate Chin under head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_CHIN_SIZE/2f, -LAMP_HEAD_HEIGHT/2f - LAMP_CHIN_SIZE*3f/2f, 0f)));
  }
  private void initialiseTranformNodes_L2(){
    translateLowerArm = translateByHeight(LAMP_BASE_HEIGHT + LAMP_ARM_LENGTH/2f, "Translate Onto Base");
    translateJoint = translateByHeight(LAMP_ARM_LENGTH/2f + LAMP_JOINT_SIZE/2f, "Translate Onto Lower Arm");
    translateUpperArm = translateByHeight(LAMP_JOINT_SIZE/2f + LAMP_ARM_LENGTH/2f, "Translate Onto Joint");
    translateLampHead = translateByHeight(LAMP_ARM_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, "Translate Onto Upper Arm");

    translateSpotlight = new TransformNode("Translate Onto End of Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT + LAMP_SPOTLIGHT_LENGTH/2f,
      0f, 0f)));
    translateTail = new TransformNode("Translate behind the Joint", new Mat4(Mat4Transform.translate(-(LAMP_JOINT_SIZE/2f + LAMP_TAIL_LENGTH/2f), 0f, 0f)));

    translateLeftEye = new TransformNode("Translate Left Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
    translateRightEye = new TransformNode("Translate Right Eye on Head", new Mat4(Mat4Transform.translate(LAMP_HEAD_HEIGHT - LAMP_EYE_SIZE/2f, LAMP_EYE_SIZE/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    
    translateLeftEar = new TransformNode("Translate Left Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, LAMP_HEAD_HEIGHT/3f)));
    translateRightEar = new TransformNode("Translate Right Ear on Head", new Mat4(Mat4Transform.translate(-LAMP_HEAD_HEIGHT + LAMP_EYE_SIZE/2f, LAMP_EAR_LENGTH/2f + LAMP_HEAD_HEIGHT/2f, -LAMP_HEAD_HEIGHT/3f)));
    
    translateLimbLeft = new TransformNode("Translate Limb Left", new Mat4(Mat4Transform.translate(0f, 0f, -LAMP_LIMB_LENGTH/2f - LAMP_ARM_WIDTH/2f)));
    translateLimbRight = new TransformNode("Translate Limb Right", new Mat4(Mat4Transform.translate(0f, 0f, LAMP_LIMB_LENGTH/2f + LAMP_ARM_WIDTH/2f)));

  }

  // Rotate nodes at each joint
  private TransformNode rotateLowerArmY;
  private TransformNode rotateLowerArmZ;
  private TransformNode rotateJoint;
  private TransformNode rotateHead;
  private TransformNode rotateEar;
  
  private TransformNode rotateLimbLeft;
  private TransformNode rotateLimbRight;

  private void initialiseRotateNodes_L1() {
    rotateLowerArmZ = new TransformNode("Rotate Lower Arm Z", Mat4Transform.rotateAroundZ(startRotationLowerArmZ));
    rotateLowerArmY = new TransformNode("Rotate Lower Arm Y", Mat4Transform.rotateAroundY(startRotationLowerArmY));
    rotateJoint = new TransformNode("Rotate Joint", Mat4Transform.rotateAroundZ(startRotationJoint));
    rotateHead = new TransformNode("Rotate Head", Mat4Transform.rotateAroundZ(startRotationHead));

    Mat4 rotationEarMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(rotationEar), new Mat4(1));
    rotationEarMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(rotationEar), rotationEarMatrix);
    rotateEar = new TransformNode("Rotate Ear", rotationEarMatrix);
  }

  private void initialiseRotateNodes_L2() {
    rotateLowerArmZ = new TransformNode("Rotate Lower Arm Z", Mat4Transform.rotateAroundZ(startRotationLowerArmZ));
    rotateLowerArmY = new TransformNode("Rotate Lower Arm Y", Mat4Transform.rotateAroundY(startRotationLowerArmY));
    rotateJoint = new TransformNode("Rotate Joint", Mat4Transform.rotateAroundZ(startRotationJoint));
    rotateHead = new TransformNode("Rotate Head", Mat4Transform.rotateAroundZ(startRotationHead));

    rotateLimbLeft = new TransformNode("Rotate Limb Left", Mat4Transform.rotateAroundX(-90f));
    rotateLimbRight = new TransformNode("Rotate Limb Left", Mat4Transform.rotateAroundX(90f));
  }

  public void initialiseAnimation(int animation) {
    this.isAnimating = true;
    //reset startAnglePositions
    setStartRotations();
    frames = 0;
    switch(animation) {
      case 1:
        // first pos of first lamp
        activeEndRotationLowerArmY = LAMP1_POS1_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP1_POS1_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP1_POS1_JOINT;
        activeEndRotationHead = LAMP1_POS1_HEAD;
      break;
      case 2:
        // second pos of first lamp
        activeEndRotationLowerArmY = LAMP1_POS2_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP1_POS2_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP1_POS2_JOINT;
        activeEndRotationHead = LAMP1_POS2_HEAD;
      break;
      case 3:
        // third pos of first lamp
        activeEndRotationLowerArmY = LAMP1_POS3_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP1_POS3_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP1_POS3_JOINT;
        activeEndRotationHead = LAMP1_POS3_HEAD;
      break;
      case 4:
        activeEndRotationLowerArmY = LAMP2_POS1_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP2_POS1_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP2_POS1_JOINT;
        activeEndRotationHead = LAMP2_POS1_HEAD;
      break;
      case 5:
        activeEndRotationLowerArmY = LAMP2_POS2_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP2_POS2_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP2_POS2_JOINT;
        activeEndRotationHead = LAMP2_POS2_HEAD;
      break;
      case 6:
        activeEndRotationLowerArmY = LAMP2_POS3_LOWER_ARM_Y;
        activeEndRotationLowerArmZ = LAMP2_POS3_LOWER_ARM_Z;
        activeEndRotationJoint = LAMP2_POS3_JOINT;
        activeEndRotationHead = LAMP2_POS3_HEAD;
      break;
    }
  }

  private void setStartRotations() {
    startRotationLowerArmY = currentRotationLowerArmY;
    startRotationLowerArmZ = currentRotationLowerArmZ;
    startRotationJoint = currentRotationJoint;
    startRotationHead = currentRotationHead;
  }

  public void animate() {
    if (frames > maxFrames) {
      isAnimating = false;
      frames = 0;
      
      // set the rotations to the current one for smooth 
      // changin between animations
      startRotationLowerArmY = activeEndRotationLowerArmY;
      startRotationLowerArmZ = activeEndRotationLowerArmZ;
      startRotationJoint = activeEndRotationJoint;
      startRotationHead = activeEndRotationHead;

      return;
    }
    float fr = frames/maxFrames;

    currentRotationLowerArmY = lerp(startRotationLowerArmY, activeEndRotationLowerArmY, fr);
    currentRotationLowerArmZ = lerp(startRotationLowerArmZ, activeEndRotationLowerArmZ, fr);
    currentRotationJoint = lerp(startRotationJoint, activeEndRotationJoint, fr);
    currentRotationHead = lerp(startRotationHead, activeEndRotationHead, fr);

    rotateLowerArmY.setTransform(Mat4Transform.rotateAroundY(currentRotationLowerArmY));
    rotateLowerArmZ.setTransform(Mat4Transform.rotateAroundZ(currentRotationLowerArmZ));
    rotateJoint.setTransform(Mat4Transform.rotateAroundZ(currentRotationJoint));
    rotateHead.setTransform(Mat4Transform.rotateAroundZ(currentRotationHead));

    updateSpotlightDirection(currentRotationLowerArmY, currentRotationJoint + currentRotationHead + currentRotationLowerArmZ);
    root.update();
    updateSpotlightPosition();
    frames ++;
  }

  private void updateSpotlightDirection(float yRotation, float zRotation) {
    updateHeadFrontVector(0f, -spotlightDirAdjust * (float)Math.toRadians(yRotation), (float)Math.toRadians(zRotation));
    lampSpotlight.getSpotlightNode().setSpotlightDirection(frontHeadVector);
  }

  private void updateSpotlightPosition() {
    lampSpotlight.getSpotlightNode().setSpotlightPosition(lampSpotlight.getSpotlightNode().getWorldTransform().getLastColumn());
  }

  public void setIsAnimating(boolean animate) {
    this.isAnimating = animate;
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
    frontHeadVector.x = spotlightDirAdjust * (float)(cy*cp);
    frontHeadVector.y = (float)(sp);
    frontHeadVector.z = (float)(sy*cp);
    frontHeadVector.normalize();
  } 

  private void makeSceneGraph_L1(){
    root.addChild(translateToPosition); // translates to -3, 0, 0
    translateToPosition.addChild(rotateLamp);
      rotateLamp.addChild(base.getNameNode()); // add base node
        base.getNameNode().addChild(rotateLowerArmY);
        //add rotatelower arm z
          rotateLowerArmY.addChild(rotateLowerArmZ);
          rotateLowerArmZ.addChild(translateLowerArm);
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
                              lampHead.getNameNode().addChild(translateChin);
                                translateChin.addChild(chin.getNameNode());
                                translateLeftEar.addChild(earLeft.getNameNode());
                                translateRightEye.addChild(eyeRight.getNameNode());
                                translateLeftEye.addChild(eyeLeft.getNameNode());
                                translateSpotlight.addChild(lampSpotlight.getNameNode());
    root.update();
  }
  private void makeSceneGraph_L2(){
    root.addChild(translateToPosition); // translates to -3, 0, 0
    translateToPosition.addChild(rotateLamp);
      rotateLamp.addChild(base.getNameNode()); // add base node
        base.getNameNode().addChild(rotateLowerArmY);
        //add rotatelower arm z
          rotateLowerArmY.addChild(rotateLowerArmZ);
          rotateLowerArmZ.addChild(translateLowerArm);
            translateLowerArm.addChild(lowerArm.getNameNode());
              lowerArm.getNameNode().addChild(translateJoint);
                translateJoint.addChild(rotateJoint);
                  rotateJoint.addChild(sphereJoint.getNameNode());
                    sphereJoint.getNameNode().addChild(translateUpperArm);
                    sphereJoint.getNameNode().addChild(translateTail);
                      translateTail.addChild(tail.getNameNode());
                      translateUpperArm.addChild(upperArm.getNameNode());
                        upperArm.getNameNode().addChild(translateLampHead);
                        upperArm.getNameNode().addChild(translateLimbLeft);
                        upperArm.getNameNode().addChild(translateLimbRight);
                          translateLimbLeft.addChild(rotateLimbLeft);
                            rotateLimbLeft.addChild(limbLeft.getNameNode());
                          translateLimbRight.addChild(rotateLimbRight);
                            rotateLimbRight.addChild(limbRight.getNameNode());
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
