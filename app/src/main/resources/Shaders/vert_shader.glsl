#version 460 core
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec2 aTexturePos;

out vec2 texPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

uniform float scale;

void main() {
    gl_Position = proj * view * model * vec4(aPos, scale);
    texPos = aTexturePos;
}