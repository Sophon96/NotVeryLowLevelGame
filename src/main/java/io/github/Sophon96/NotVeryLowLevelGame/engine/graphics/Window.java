package io.github.Sophon96.NotVeryLowLevelGame.engine.graphics;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final long handle;

    public Window() {
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode monitorVideoMode = glfwGetVideoMode(monitor);
        assert monitorVideoMode != null;
        int monitorWidth = monitorVideoMode.width(), monitorHeight = monitorVideoMode.height();
        int windowHeight = monitorHeight;//(int) (monitorHeight * 0.75f);
        int windowWidth = monitorWidth;//(int) (monitorWidth * 0.75f);

        long handle = glfwCreateWindow(windowWidth, windowHeight, "Window Title Goes Here", monitor, NULL);
        if (handle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // UNLIMITED cursor movement
        glfwPollEvents();
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        //glfwSwapInterval(1);    // vsync
        glfwShowWindow(handle);

        this.handle = handle;
    }

    public long getHandle() {
        return handle;
    }

    public void swapAndPoll() {
        glfwPollEvents();
        glfwSwapBuffers(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void destroy() {
        glfwDestroyWindow(handle);
    }
}
