package core.constants;
import gmaths.*;

public final class Constants {
  public static final float WALL_SIZE = 16f;


  //table dimensions
  public static final float TABLE_LENGTH = 2f;
  public static final float TABLE_HEIGHT = 2f;
  // egg dimensions
  public static final float EGG_WIDTH = 1.3f;
  public static final float EGG_HEIGHT = 2f;
  public static final float EGG_BASE_HEIGHT = 0.5f;

  // egg Material properties
  // public static final float EGG_MATERIAL;

  //spotlight positions

  // lamp positions
  // lamp dimensions
  public static final float LAMP_BASE_HEIGHT = .3f;
  public static final float LAMP_ARM_SIZE = 2f;
  public static final float LAMP_JOINT_SIZE = .3f;
  public static final float LAMP_HEAD_HEIGHT = .5f;
  public static final float LAMP_SPOTLIGHT_LENGTH = LAMP_HEAD_HEIGHT/2f;
  public static final float LAMP_TAIL_LENGTH = 1f;

  public static final float LAMP_EYE_SIZE = LAMP_HEAD_HEIGHT/2f;

  public static final Vec3 LAMP1_POSITION = new Vec3(-3,0,0);
  public static final Vec3 LAMP2_POSITION = new Vec3(3,0,0);

  // Lamp Animation variables
  public static final float LAMP1_POS1_LOWER_ARM = -60f;
  public static final float LAMP1_POS1_JOINT = -60f;
  public static final float LAMP1_POS1_HEAD = 30f;

  public static final float LAMP1_POS2_LOWER_ARM = 0f;
  public static final float LAMP1_POS2_JOINT = 120f;
  public static final float LAMP1_POS2_HEAD = 200f;

  public static final float LAMP1_POS3_LOWER_ARM = 90f;
  public static final float LAMP1_POS3_JOINT = -30f;
  public static final float LAMP1_POS3_HEAD = 20f;

  public static final float LAMP2_POS1_LOWER_ARM = -60f;
  public static final float LAMP2_POS1_JOINT = -60f;
  public static final float LAMP2_POS1_HEAD = 30f;

  public static final float LAMP2_POS2_LOWER_ARM = 0f;
  public static final float LAMP2_POS2_JOINT = 120f;
  public static final float LAMP2_POS2_HEAD = 200f;

  public static final float LAMP2_POS3_LOWER_ARM = 90f;
  public static final float LAMP2_POS3_JOINT = -30f;
  public static final float LAMP2_POS3_HEAD = 20f;
}
