#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
out vec4 fragColor;

uniform vec3 Gray = vec3(0.3, 0.59, 0.11);
uniform vec3 RedMatrix = vec3(1.0, 0.0, 0.0);
uniform vec3 GreenMatrix = vec3(0.0, 1.0, 0.0);
uniform vec3 BlueMatrix = vec3(0.0, 0.0, 1.0);
uniform vec3 Offset = vec3(0.0, 0.0, 0.0);
uniform vec3 ColorScale = vec3(1.0, 1.0, 1.0);
uniform float Saturation = 1.0;

void main() {
    vec4 inTexel = texture(DiffuseSampler, texCoord);

    float redValue = dot(inTexel.rgb, RedMatrix);
    float greenValue = dot(inTexel.rgb, GreenMatrix);
    float blueValue = dot(inTexel.rgb, BlueMatrix);
    vec3 outColor = vec3(redValue, greenValue, blueValue);

    outColor = (outColor * ColorScale) + Offset;

    float luma = dot(outColor, Gray);
    vec3 chroma = outColor - luma;
    outColor = (chroma * Saturation) + luma;

    fragColor = vec4(outColor, inTexel.a);
}
