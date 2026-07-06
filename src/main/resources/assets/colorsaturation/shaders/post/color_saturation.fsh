#version 330

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

layout(std140) uniform SaturationConfig {
    float Saturation;
};

void main() {
    vec4 inTexel = texture(DiffuseSampler, texCoord);

    vec3 luma = vec3(dot(inTexel.rgb, vec3(0.3, 0.59, 0.11)));
    vec3 outColor = mix(luma, inTexel.rgb, Saturation);

    fragColor = vec4(outColor, inTexel.a);
}
