package io.github.Sophon96.NotVeryLowLevelGame.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private float sensitivity = 0.1f;
    private Vector3f cameraPos, cameraFront, cameraUp;
    private float yaw = -90.f, pitch = 0.f;

    public Camera(Vector3f cameraPos, Vector3f cameraFront, Vector3f cameraUp) {
        this.cameraPos = cameraPos;
        this.cameraFront = cameraFront;
        this.cameraUp = cameraUp;
    }

    public Camera() {
        this(new Vector3f(0, 5, 0), new Vector3f(0, -0.99f, 0).normalize(), new Vector3f(0, 1, 0));
    }

    public void move(Vector3f delta) {
        cameraPos.add(delta);
    }

    public void moveForward(float speed, boolean free) {
        Vector3f forward = new Vector3f(cameraFront);
        if (!free) {
            forward.y = 0f;
            forward.normalize();
        }
        cameraPos.add(forward.mul(speed));
    }

    public void moveBack(float speed, boolean free) {
        Vector3f forward = new Vector3f(cameraFront);
        if (!free) {
            forward.y = 0f;
            forward.normalize();
        }
        cameraPos.sub(forward.mul(speed));
    }

    public void moveLeft(float speed, boolean free) {
        Vector3f forward = new Vector3f(cameraFront);
        if (!free) {
            forward.y = 0f;
            forward.normalize();
        }
        Vector3f right = forward.cross(cameraUp).normalize();
        cameraPos.sub(new Vector3f(right).mul(speed));
    }

    public void moveRight(float speed, boolean free) {
        Vector3f forward = new Vector3f(cameraFront);
        if (!free) {
            forward.y = 0f;
            forward.normalize();
        }
        Vector3f right = forward.cross(cameraUp).normalize();
        cameraPos.add(new Vector3f(right).mul(speed));
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(cameraPos, new Vector3f(cameraPos).add(cameraFront), cameraUp);
    }

    public void updateLookFromDelta(float dx, float dy) {
        yaw += sensitivity * dx;
        pitch += sensitivity * dy;

        pitch = Math.clamp(pitch, -89.f, 89.f);

        Vector3f direction = new Vector3f();
        direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        direction.y = (float) Math.sin(Math.toRadians(pitch));
        direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));

        cameraFront.set(direction.normalize());
    }

    public Vector3f getCameraPos() {
        return new Vector3f(cameraPos);
    }

    public Vector3f getCameraFront() {
        return new Vector3f(cameraFront);
    }

    public Vector3f getCameraUp() {
        return new Vector3f(cameraUp);
    }

    public Vector3f getCameraRight() {
        return new Vector3f(cameraFront).cross(cameraUp).normalize();
    }
}
