#version 330 core
in vec2 TexCoord;
in vec3 Normal;
in vec3 FragPos;

out vec4 FragColor;
uniform vec3 lightPos;

uniform sampler2D texture1;

void main() {
    // Ambient light
    float ambientStrength = 0.5;
    vec3 ambient = ambientStrength * vec3(1.0, 1.0, 1.0);

    // Diffuse light
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(lightPos - FragPos);

    // Calculate the angle (Dot Product). "max(..., 0.0)" prevents negative light.
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * vec3(1.0, 1.0, 1.0);

    // Combine
    vec4 objectColor = texture(texture1, TexCoord);
    vec3 result = (ambient + diffuse) * objectColor.rgb;

    vec4 fragColor = vec4(result, objectColor.a);

    float gamma = 2.2;
    FragColor.rgb = pow(fragColor.rgb, vec3(1.0/gamma));
    FragColor.a = fragColor.a;
}