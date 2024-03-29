package core.objects.primitives;
/**
 * Class from COM3503 Online Tutorial
 * by Dr Steve Maddock at The University of Sheffield, 2022.
 *
 */
public final class Surface {
  // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering
  public static final float[] VERTICES = {      // position, colour, tex coords
    -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 1.0f,  // top left
    -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,  // bottom left
     0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 0.0f,  // bottom right
     0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  1.0f, 1.0f   // top right
  };
  
  public static final int[] INDICES = {         // Note that we start from 0
      0, 1, 2,
      0, 2, 3
  };
}
