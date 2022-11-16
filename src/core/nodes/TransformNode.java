package core.nodes;

import gmaths.Mat4;

public class TransformNode extends SGNode {

    private Mat4 transform;

    public TransformNode(String name, Mat4 t) {
        super(name);
        transform = new Mat4(t);
    }

    public void setTransform(Mat4 m) {
        transform = new Mat4(m);
    }

    public Mat4 getTransform() {
        return transform;
    }

    protected void update(Mat4 t) {
        worldTransform = t;
        t = Mat4.multiply(worldTransform, transform);
        for (SGNode child : children) {
            child.update(t);
        }
    }

    public void print(int indent, boolean inFull) {
        System.out.println(getIndentString(indent) + "Name: " + name);
        if (inFull) {
            System.out.println("worldTransform");
            System.out.println(worldTransform);
            System.out.println("transform node:");
            System.out.println(transform);
        }
        for (SGNode child : children) {
            child.print(indent + 1, inFull);
        }
    }
}