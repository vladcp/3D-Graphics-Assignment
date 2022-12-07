package core.rendering;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.jogamp.opengl.*;

import com.jogamp.opengl.util.texture.*;
/**
 * Texture Library class,
 * adapted from COM3503 Online Tutorial Materials
 * by Dr Steve Maddock at The University of Sheffield, 2022.
 */
public final class TextureLibrary {

  public static Texture loadTexture(GL3 gl3, String filename) {
    return loadTexture(gl3, filename, GL3.GL_REPEAT, GL3.GL_REPEAT, GL3.GL_LINEAR_MIPMAP_LINEAR, GL3.GL_LINEAR);
  }

  // mip-mapping is included
  public static Texture loadTexture(GL3 gl3, String filename, 
                                    int wrappingS, int wrappingT, int filterS, int filterT) {
    Texture t = null; 
    try {
      File f = new File(filename);
      t = (Texture)TextureIO.newTexture(f, true);
      t.bind(gl3);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_S, wrappingS);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_WRAP_T, wrappingT); 
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_MIN_FILTER, filterS);
      t.setTexParameteri(gl3, GL3.GL_TEXTURE_MAG_FILTER, filterT);
      gl3.glGenerateMipmap(GL3.GL_TEXTURE_2D);
    }
    catch(Exception e) {
      System.out.println("Error loading texture " + filename); 
      System.out.println("EXCEPTION: " + e); 
    }
    return t;
  }

  public static final int FLOOR_TEXTURE = 0;
  public static final int WALL_TEXTURE = 1;
  public static final int WINDOW_GROUND = 2;
  public static final int WINDOW_CLOUDS = 3;
  public static final int CONTAINER = 4;
  public static final int EGG = 5;
  public static final int EGG_SPECULAR = 6;
  public static final int PLANET = 7;
  public static final int DROPLETS = 8;
  public static final int TABLE = 9;
  public static final int JAGUAR = 10;
  public static final int SPOTTED = 11;
  /**
     * Load all textures in a list
     *
     * @return texture List<Texture>
     */
    public static List<Texture> populateTextureList(GL3 gl){
      List<Texture> textureList = new ArrayList<>();

      textureList.add(TextureLibrary.loadTexture(gl, "./textures/floor.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/floorTexture.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/ground.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/sky-moving.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/container.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/jade.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/jade_specular.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/planet.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/droplets.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/table.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/jaguar.jpg"));
      textureList.add(TextureLibrary.loadTexture(gl, "./textures/spotted.jpeg"));

      return textureList;
  }
}