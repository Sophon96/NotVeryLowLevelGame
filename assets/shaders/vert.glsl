#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormal;
layout (location = 2) in vec2 aTexCoord;

out vec2 TexCoord;
out vec3 Normal;
out vec3 FragPos;

uniform mat4 uMVP;
uniform mat4 uModel;

void main() {
    gl_Position = uMVP * vec4(aPos, 1.0);

    TexCoord = aTexCoord;

    FragPos = vec3(uModel * vec4(aPos, 1.0));

    // Technically we should use "mat3(transpose(inverse(uModel)))"
    // to handle non-uniform scaling, but for now, this is fine:
    Normal = mat3(uModel) * aNormal;
}
