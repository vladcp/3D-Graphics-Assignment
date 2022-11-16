#version 330 core

in vec3 aPos;
in vec3 aNormal;
in vec2 aTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;
uniform sampler2D second_texture;

void main() {
    fragColor = vec4(vec3(texture(first_texture, aTexCoord)), 1.0f);
}