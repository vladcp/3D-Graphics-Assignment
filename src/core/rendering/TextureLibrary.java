package core.rendering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.*;

import com.jogamp.opengl.util.texture.*;

public final class TextureLibrary {
    
  public static final int FLOOR_WOOD = 0;
  public static final int WINDOW_GROUND = 1;
  public static final int WINDOW_CLOUDS = 2;
  public static final int CONTAINER = 3;
  public static final int EGG = 4;
  public static final int EGG_SPECULAR = 5;
  // mip-mapping is included
  public static Texture loadTexture(GL3 gl3, String filename) {
    Texture t = null; 
    try {
      File f = new File(filename);
      t = (Texture)TextureIO.newTexture(f, true);
      t.bind(gl3);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT); 
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, GL3.GL_LINEAR_MIPMAP_LINEAR);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, GL3.GL_LINEAR);
      gl3.glGenerateMipmap(GL3.GL_TEXTURE_2D);
    }
    catch(Exception e) {
      System.out.println("Error loading texture " + filename); 
      System.out.println("EXCEPTION: " + e); 
    }
    return t;
  }

  /**
     * Load all textures in a list
     *
     * @return texture List<Texture>
     */
    public static List<Texture> populateTextureList(GL3 gl){
      List<Texture> textureList = new ArrayList<>();

      textureList.add(TextureLibrary.loadTexture(gl, "./textures/woodtex.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/ground.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/sky-moving.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/container2.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/shiningEgg.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/shiningEggSpecular.jpg"));

      return textureList;
  }
}