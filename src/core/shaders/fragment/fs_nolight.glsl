#version 330 core

in vec3 aPos;
in vec3 aNormal;
in vec2 aTexCoord;
in vec2 movingTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;

void main() {
  // fragColor = vec4(mix(texture(first_texture, aTexCoord),
  //                      texture(second_texture, movingTexCoord),
  //                  0.5f).rgb, 1.0f);
  fragColor = vec4(vec3(texture(first_texture, movingTexCoord)), 1.0f);
}