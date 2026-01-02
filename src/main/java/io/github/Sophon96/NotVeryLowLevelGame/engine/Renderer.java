package io.github.Sophon96.NotVeryLowLevelGame.engine;

import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Shader;
import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Renderer {
    private Shader shader;

    public Renderer(Shader shader) {
        this.shader = shader;
    }

    public void renderThings(List<Thing> things, Matrix4f view) {
        shader.use();

        Matrix4f proj = new Matrix4f().perspective((float) Math.toRadians(45.0f), 1600f / 1000f, 0.1f, 100.0f);

        // Diffuse lighting position
        shader.setUniform("lightPos", new Vector3f(2.0f, 100.0f, 5.0f));

        for (Thing thing : things) {
            Mesh mesh = thing.getMesh();
            Texture texture = thing.getTexture();
            Transform transform = thing.getTransform();

            // Combine them: P * V * M
            Matrix4f model = transform.getTransformationMatrix();
            Matrix4f mvp = new Matrix4f(proj).mul(view).mul(model);

            // Upload the matrix to the Shader "uniform"
            shader.setUniform("uMVP", mvp);
            shader.setUniform("uModel", model);

            // Activate the texture? not sure if this needed
            glActiveTexture(GL_TEXTURE0);
            // Bined texture
            texture.bind();

            mesh.render();
        }
    }
}
