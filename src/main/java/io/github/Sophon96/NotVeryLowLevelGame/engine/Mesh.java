package io.github.Sophon96.NotVeryLowLevelGame.engine;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.ARBVertexArrayObject.glGenVertexArrays;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class Mesh {
    private final int vaoId;
    private final int vertexCount;
    private final int indicesCount;

    public static Mesh loadMesh(String modelId, String modelPath) {
        //File file = new File(modelPath);
        //if (!file.exists()) {
        //    throw new RuntimeException("Model path does not exist: " + modelPath);
        //}

        AIScene aiScene = aiImportFile(modelPath, aiProcess_JoinIdenticalVertices |
                aiProcess_Triangulate |
                aiProcess_FixInfacingNormals);
        if (aiScene == null || aiScene.mRootNode() == null) {
            throw new RuntimeException("Could not load model: " + modelPath);
        }

        AIMesh aiMesh = AIMesh.create(aiScene.mMeshes().get(0));

        List<Float> positions = new ArrayList<>();
        List<Float> textures = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        // --- Process Vertices, Normals, and UVs ---
        AIVector3D.Buffer aiVertices = aiMesh.mVertices();
        AIVector3D.Buffer aiNormals = aiMesh.mNormals();

        // Texture coords can have multiple channels (0-7). We usually only care about 0.
        AIVector3D.Buffer aiTextCoords = aiMesh.mTextureCoords(0);

        while (aiVertices.remaining() > 0) {
            AIVector3D aiVertex = aiVertices.get();
            positions.add(aiVertex.x());
            positions.add(aiVertex.y());
            positions.add(aiVertex.z());

            // Check if normals exist
            if (aiNormals != null) {
                AIVector3D aiNormal = aiNormals.get();
                normals.add(aiNormal.x());
                normals.add(aiNormal.y());
                normals.add(aiNormal.z());
            }

            // Check if texture coordinates exist
            if (aiTextCoords != null) {
                AIVector3D aiTex = aiTextCoords.get();
                textures.add(aiTex.x());
                textures.add(aiTex.y()); // sometimes 1 - y if textures look upside down
            } else {
                textures.add(0.0f);
                textures.add(0.0f);
            }
        }

        // --- Process Indices (Faces) ---
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        while (aiFaces.remaining() > 0) {
            AIFace aiFace = aiFaces.get();
            if (aiFace.mNumIndices() != 3) {
                // Should not happen due to aiProcess_Triangulate
                continue;
            }
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }

        // Convert Lists to primitive arrays for your Mesh class
        return new Mesh(
                toFloatArray(positions),
                toFloatArray(normals),
                toFloatArray(textures),
                toIntArray(indices)
        );
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indicesCount, GL_UNSIGNED_INT, 0);
    }

    public void cleanup() {

    }

    private static float[] toFloatArray(List<Float> list) {
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private static int[] toIntArray(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    private Mesh(float[] positions, float[] normals, float[] textures, int[] indices) {
        assert positions.length % 3 == 0;
        assert normals.length % 3 == 0;
        assert textures.length % 2 == 0;

        assert positions.length / 3 == normals.length / 3;
        assert positions.length / 3 == textures.length / 2;

        float[] vertices = new float[positions.length / 3 * 8];
        for (int i = 0; i < positions.length / 3; i += 1) {
            vertices[i * 8 + 0] = positions[i * 3 + 0];
            vertices[i * 8 + 1] = positions[i * 3 + 1];
            vertices[i * 8 + 2] = positions[i * 3 + 2];
            vertices[i * 8 + 3] = normals[i * 3 + 0];
            vertices[i * 8 + 4] = normals[i * 3 + 1];
            vertices[i * 8 + 5] = normals[i * 3 + 2];
            vertices[i * 8 + 6] = textures[i * 2 + 0];
            vertices[i * 8 + 7] = textures[i * 2 + 1];
        }
        vertexCount = vertices.length;

        // Create VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create VBO
        int vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW); // "Static" means we won't change it often

        // Create EBO
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
        indicesCount = indices.length;

        // Tell OpenGL how to read the VBO (Attributes)
        // Position (Layout 0, 3 floats)
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Normals (Layout 1, 3 floats, offset by 3 floats)
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // Texture (Layout 2)
        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, 6 * Float.BYTES);
        glEnableVertexAttribArray(2);

        // 7. Unbind to keep things clean
        glBindVertexArray(0);
    }
}
