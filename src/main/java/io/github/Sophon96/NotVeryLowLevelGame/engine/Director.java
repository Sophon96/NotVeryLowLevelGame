package io.github.Sophon96.NotVeryLowLevelGame.engine;

import io.github.Sophon96.NotVeryLowLevelGame.engine.graphics.Window;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Director {
    private Window window;
    private Scene scene;

    public Director() {
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = new Window();
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void changeScene(Scene scene) {
        if (this.scene != null) {
            this.scene.onExit();
        }
        this.scene = scene;
        this.scene.onEnter();
    }

    public void run() {
        while (!window.shouldClose()) {
            scene.update();
            scene.render();

            // E. Swap and Poll
            window.swapAndPoll(); // Show the new frame
        }
    }
}
