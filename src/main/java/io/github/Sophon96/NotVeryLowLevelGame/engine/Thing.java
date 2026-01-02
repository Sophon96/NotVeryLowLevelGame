package io.github.Sophon96.NotVeryLowLevelGame.engine;

import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Texture;

public class Thing {
    private Mesh mesh;
    private Texture texture;
    private Transform transform;

    public Thing(Mesh mesh, Texture texture) {
        this(mesh, texture, new Transform());
    }

    public Thing(Mesh mesh, Texture texture, Transform transform) {
        this.mesh = mesh;
        this.texture = texture;
        this.transform = transform;
    }

    public void update() {}

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
