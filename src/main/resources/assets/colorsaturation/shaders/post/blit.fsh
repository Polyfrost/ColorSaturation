#version 330
//? if >=26.3
#extension GL_ARB_separate_shader_objects : require

uniform sampler2D DiffuseSampler;

//? if >=26.3 {
layout(location = 0) in vec2 texCoord;

layout(location = 0) out vec4 fragColor;
//?}
//? if <26.3 {
//in vec2 texCoord;
//
//out vec4 fragColor;
//?}

void main() {
    fragColor = texture(DiffuseSampler, texCoord);
}
