package core.objects.final_objects.alien_lamps;

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
  EYE_RIGHT("Eye Right");

  public final String NAME;
  private LampComponentName(String name){
    NAME = name;
  }

  public String toString(){
    return NAME;
  }
}
