package core.nodes;

import com.jogamp.opengl.GL3;

import core.rendering.Model;
import gmaths.*;

public class ModelNode extends SGNode {

    protected Model model;

    public ModelNode(String name, Model m) {
        super(name);
        model = m;
    }

    public void draw(GL3 gl) {
        model.render(gl, worldTransform);
        for (SGNode child : children) {
            child.draw(gl);
        }
    }

    public Mat4 getModelMatrix() {
        return model.getModelMatrix();
    }

    public Vec3 getCurrentPosition() {
        return model.getCurrentPosition();
    }
}