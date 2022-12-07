package core.constants;
import gmaths.*;

public final class Constants {
  public static final float WALL_SIZE = 16f;

  public static final float LIGHT_SIZE = 0.3f;

  public static final Vec3 DEFAULT_POSITION_1 = new Vec3(-8 + LIGHT_SIZE/2f, LIGHT_SIZE/2f, 8);
  public static final Vec3 DEFAULT_POSITION_2 = new Vec3(8 - LIGHT_SIZE/2f, 16, -8 + LIGHT_SIZE/2f);

  //table dimensions
  public static final float TABLE_LENGTH = 2f;
  public static final float TABLE_HEIGHT = 2f;
  // egg dimensions
  public static final float EGG_WIDTH = 1.3f * 1.4f;
  public static final float EGG_HEIGHT = 2f * 1.4f;
  public static final float EGG_BASE_HEIGHT = .8f;


  // lamp positions
  // lamp dimensions
  public static final float LAMP_BASE_HEIGHT = .3f;
  public static final float LAMP_ARM_LENGTH = 2f;
  public static final float LAMP_ARM_WIDTH = 0.3f;

  public static final float LAMP_JOINT_SIZE = .3f;
  public static final float LAMP_HEAD_HEIGHT = .5f;
  public static final float LAMP_SPOTLIGHT_LENGTH = LAMP_HEAD_HEIGHT/2f;
  public static final float LAMP_TAIL_LENGTH = 1f;
  
  public static final float LAMP_EYE_SIZE = LAMP_HEAD_HEIGHT/2f;
  public static final float LAMP_EAR_LENGTH = LAMP_HEAD_HEIGHT * 3;
  
  public static final float LAMP_CHIN_SIZE = LAMP_EYE_SIZE;

  // lamp2 
  public static final float LAMP_LIMB_LENGTH = LAMP_ARM_LENGTH / 1.3f;
  public static final float LAMP_LIMP_WIDTH = LAMP_ARM_WIDTH / 2f;
  // Lamp Positions and Rotations
  public static final Vec3 LAMP1_POSITION = new Vec3(-4,0,0);
  public static final float LAMP1_ROTATION = 0f;

  public static final Vec3 LAMP2_POSITION = new Vec3(4,0,0);
  public static final float LAMP2_ROTATION = 180f;

  // Lamp Animation variables
  // Lamp 1
  public static final float LAMP1_POS1_LOWER_ARM_Y = 0f;
  public static final float LAMP1_POS1_LOWER_ARM_Z = 20f;
  public static final float LAMP1_POS1_JOINT = -45f;
  public static final float LAMP1_POS1_HEAD = 15f;
  
  public static final float LAMP1_POS2_LOWER_ARM_Y = -180f;
  public static final float LAMP1_POS2_LOWER_ARM_Z = -30f;
  public static final float LAMP1_POS2_JOINT = 60f;
  public static final float LAMP1_POS2_HEAD = 30f;

  public static final float LAMP1_POS3_LOWER_ARM_Y = 0f;
  public static final float LAMP1_POS3_LOWER_ARM_Z = 60f;
  public static final float LAMP1_POS3_JOINT = -60f;
  public static final float LAMP1_POS3_HEAD = 20f;

  // Lamp 2
  public static final float LAMP2_POS1_LOWER_ARM_Y = -60f;
  public static final float LAMP2_POS1_LOWER_ARM_Z = 0f;
  public static final float LAMP2_POS1_JOINT = -60f;
  public static final float LAMP2_POS1_HEAD = 30f;

  public static final float LAMP2_POS2_LOWER_ARM_Y = 120f;
  public static final float LAMP2_POS2_LOWER_ARM_Z = -20f;
  public static final float LAMP2_POS2_JOINT = -30f;
  public static final float LAMP2_POS2_HEAD = 30;

  public static final float LAMP2_POS3_LOWER_ARM_Y = 0f;
  public static final float LAMP2_POS3_LOWER_ARM_Z = 5f;
  public static final float LAMP2_POS3_JOINT = -30f;
  public static final float LAMP2_POS3_HEAD = 20f;
}
