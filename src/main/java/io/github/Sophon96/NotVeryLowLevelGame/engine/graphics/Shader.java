package io.github.Sophon96.NotVeryLowLevelGame.engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {
    private final int id;

    public Shader(String vertPath, String fragPath) throws IOException {
        id = glCreateProgram();
        if (id == 0) {
            System.err.println("Could not create shader.");
        }

        int vertShader = createShader(Files.readString(Path.of(vertPath)), GL_VERTEX_SHADER);
        int fragShader = createShader(Files.readString(Path.of(fragPath)), GL_FRAGMENT_SHADER);

        linkProgram(vertShader, fragShader);
    }

    private int createShader(String shaderCode, int shaderType) {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            System.err.println("Could not create shader.");
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            System.err.println("Error compiling shader: " + glGetShaderInfoLog(shaderId, GL_INFO_LOG_LENGTH));
        }

        return shaderId;
    }

    private void linkProgram(int vertId, int fragId) {
        glAttachShader(id, vertId);
        glAttachShader(id, fragId);

        glLinkProgram(id);
        if (glGetProgrami(id, GL_LINK_STATUS) == 0) {
            System.err.println("Error linking shader: " + glGetProgramInfoLog(id, GL_INFO_LOG_LENGTH));
        }

        glDetachShader(id, vertId);
        glDetachShader(id, fragId);
    }

    public void use() {
        glUseProgram(id);
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb); // Dump matrix into buffer
            int location = glGetUniformLocation(id, name);
            glUniformMatrix4fv(location, false, fb);
        }
    }

    public void setUniform(String name, Vector3f value) {
        int location = glGetUniformLocation(id, name);
        glUniform3f(location, value.x, value.y, value.z);
    }
}
