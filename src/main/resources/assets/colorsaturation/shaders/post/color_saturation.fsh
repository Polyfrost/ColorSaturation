#version 330

uniform sampler2D DiffuseSampler;

in vec2 texCoord;

out vec4 fragColor;

layout(std140) uniform SaturationConfig {
    float Saturation;
    float Contrast;
    float Brightness;
    float Hue;
};

vec3 rotateHue(vec3 color, float degrees) {
    float angle = radians(degrees);
    float cosAngle = cos(angle);
    float sinAngle = sin(angle);
    const vec3 axis = vec3(1.0 / sqrt(3.0));
    return color * cosAngle + cross(axis, color) * sinAngle + axis * dot(axis, color) * (1.0 - cosAngle);
}

void main() {
    vec4 inTexel = texture(DiffuseSampler, texCoord);

    vec3 outColor = inTexel.rgb * Brightness;
    outColor = ((outColor - 0.5) * Contrast) + 0.5;

    vec3 luma = vec3(dot(outColor, vec3(0.3, 0.59, 0.11)));
    outColor = mix(luma, outColor, Saturation);

    outColor = rotateHue(outColor, Hue);

    fragColor = vec4(outColor, inTexel.a);
}
