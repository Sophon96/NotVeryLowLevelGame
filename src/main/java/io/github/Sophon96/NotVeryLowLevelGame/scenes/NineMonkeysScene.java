package io.github.Sophon96.NotVeryLowLevelGame.scenes;

import io.github.Sophon96.NotVeryLowLevelGame.engine.*;
import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Camera;
import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Shader;
import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Texture;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LESS;

public class NineMonkeysScene extends Scene {
    private final Renderer renderer;
    private final Camera camera;
    private float lastX, lastY;
    private List<Thing> things;

    public NineMonkeysScene(Director director) {
        super(director);

        Shader shader;
        try {
            shader = new Shader("assets/shaders/vert.glsl", "assets/shaders/frag.glsl");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        renderer = new Renderer(shader);

        Mesh monkey = Mesh.loadMesh("", "assets/monkey.glb");

        Texture texture;
        try {
            texture = new Texture("assets/textures/enemy2_16.png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Thing thing1 = new Thing(monkey, texture, new Transform(new Vector3f(-3, 0, -3), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing2 = new Thing(monkey, texture, new Transform(new Vector3f(0, 0, -3), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing3 = new Thing(monkey, texture, new Transform(new Vector3f(3, 0, -3), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing4 = new Thing(monkey, texture, new Transform(new Vector3f(-3, 0, 0), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing5 = new Thing(monkey, texture, new Transform(new Vector3f(0, 0, 0), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing6 = new Thing(monkey, texture, new Transform(new Vector3f(3, 0, 0), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing7 = new Thing(monkey, texture, new Transform(new Vector3f(-3, 0, 3), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing8 = new Thing(monkey, texture, new Transform(new Vector3f(0, 0, 3), new AxisAngle4f(-90, 1, 0, 0)));
        Thing thing9 = new Thing(monkey, texture, new Transform(new Vector3f(3, 0, 3), new AxisAngle4f(-90, 1, 0, 0)));

        things = List.of(thing1, thing2, thing3, thing4, thing5, thing6, thing7, thing8, thing9);

        camera = new Camera();
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        // A. Clear the screen (and the depth buffer)
        glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Dark gray background
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Render things (gameobjects)
        renderer.renderThings(things, camera.getViewMatrix());
    }

    @Override
    public void onEnter() {
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        // Mouse camera handling
        glfwSetCursorPosCallback(director.getWindow().getHandle(), (_window, xPos, yPos) -> {
            // cast everything to floats first
            float x = (float) xPos;
            float y = (float) yPos;

            //if (firstMouse) {
            //    lastX = x;
            //    lastY = y;
            //    firstMouse = false;
            //}

            float dx = x - lastX;
            float dy = lastY - y;
            lastX = x;
            lastY = y;

            camera.updateLookFromDelta(dx, dy);
        });
    }

    @Override
    public void onExit() {
        glDisable(GL_DEPTH_TEST);

        glfwSetCursorPosCallback(director.getWindow().getHandle(), null);
    }
}
