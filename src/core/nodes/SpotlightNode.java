package core.nodes;

import com.jogamp.opengl.GL3;

import core.lights.Spotlight;
import gmaths.*;

public class SpotlightNode extends SGNode {

    private Spotlight spotlight;

    public SpotlightNode(String name, Spotlight s) {
        super(name);
        spotlight = s;
    }

    public void draw(GL3 gl) {
        spotlight.render(gl, worldTransform);
    }

    public void setSpotlightPosition(Vec3 pos){
        spotlight.setPosition(pos);
    }

    public void setSpotlightDirection(Vec3 dir){
        spotlight.setDirection(dir);
    }
} 
