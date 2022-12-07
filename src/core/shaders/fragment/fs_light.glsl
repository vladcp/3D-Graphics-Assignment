#version 330 core

out vec4 fragColor;

uniform float lightIntensity;

void main() {
  fragColor = vec4(1.0);
  // fragColor = vec4(lightIntensity);
}