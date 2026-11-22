#version 120

uniform sampler2D DiffuseSampler;

varying vec2 texCoord;

uniform float Saturation = 1.0;
uniform float Contrast = 1.0;
uniform float Brightness = 1.0;
uniform float Hue = 0.0;

vec3 rotateHue(vec3 color, float degrees) {
    float angle = radians(degrees);
    float cosAngle = cos(angle);
    float sinAngle = sin(angle);
    vec3 axis = vec3(0.57735026);
    return color * cosAngle + cross(axis, color) * sinAngle + axis * dot(axis, color) * (1.0 - cosAngle);
}

void main() {
    vec4 inTexel = texture2D(DiffuseSampler, texCoord);

    vec3 outColor = inTexel.rgb * Brightness;
    outColor = ((outColor - 0.5) * Contrast) + 0.5;

    vec3 luma = vec3(dot(outColor, vec3(0.3, 0.59, 0.11)));
    outColor = mix(luma, outColor, Saturation);

    outColor = rotateHue(outColor, Hue);

    gl_FragColor = vec4(outColor, 1.0);
}
