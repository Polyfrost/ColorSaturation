#version 330
//? if >=26.3
#extension GL_ARB_separate_shader_objects : require

//? if >=26.3 {
layout(location = 0) in vec3 Position;

layout(location = 0) out vec2 texCoord;
//?}
//? if <26.3 {
//in vec3 Position;
//
//out vec2 texCoord;
//?}

void main() {
    gl_Position = vec4(Position.xy, 0.0, 1.0);
    texCoord = Position.xy * 0.5 + 0.5;
}
