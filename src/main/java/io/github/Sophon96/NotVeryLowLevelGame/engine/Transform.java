package io.github.Sophon96.NotVeryLowLevelGame.engine;

import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {
    private Vector3f position;
    private AxisAngle4f rotation;
    private float scale;

    public Transform() {
        this(new Vector3f(), new AxisAngle4f(), 1f);
    }

    public Transform(Vector3f position, AxisAngle4f rotation) {
        this(position, rotation, 1f);
    }

    public Transform(Vector3f position, AxisAngle4f rotation, float scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Matrix4f getTransformationMatrix() {
        return new Matrix4f().translate(position).rotate(rotation).scale(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public AxisAngle4f getRotation() {
        return rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(AxisAngle4f rotation) {
        this.rotation = rotation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
