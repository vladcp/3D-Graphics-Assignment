#version 330 core

in vec3 aPos;
in vec3 aNormal;
in vec2 aTexCoord;

out vec4 fragColor;

uniform sampler2D first_texture;
uniform sampler2D second_texture;
uniform vec3 viewPos;

struct Light {
    vec3 position;
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
};

struct Spotlight {
  vec3 position;
  vec3 ambient;
  vec3 diffuse;
  vec3 specular;

  vec3 direction;
  float cutOff;
  float outerCutOff;
};

uniform Light light1;
uniform Light light2;
uniform Spotlight spotlight1;

struct Material {
    vec3 ambient;
    vec3 diffuse;
    vec3 specular;
    float shininess;
};

uniform Material material;

vec3 calculateSpotlight(Spotlight spotlight, vec3 norm, vec3 viewDir){
  // vec3 ambient = vec3(0.0, 0.0, 0.0);
  // vec3 diffuse = vec3(0.0, 0.0, 0.0);
  // vec3 specular = vec3(0.0, 0.0, 0.0);
  vec3 ambient = spotlight.ambient * texture(first_texture, aTexCoord).rgb;
  //diffuse
  vec3 lightDir = normalize(spotlight.position - aPos);
  float diff = max(dot(norm, lightDir), 0.0);
  vec3 diffuse = spotlight.diffuse * diff * texture(first_texture, aTexCoord).rgb; 

  //specular
  vec3 reflectDir = reflect(-lightDir, norm);
  float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
  // vec3 specular = spotlight.specular * spec * texture(first_texture, aTexCoord).rgb; 
  vec3 specular = spotlight.specular * spec * texture(second_texture, aTexCoord).rgb; 

  // spotlight (soft edges)
  float theta = dot(lightDir, normalize(-spotlight.direction)); 
  float epsilon = (spotlight.cutOff - spotlight.outerCutOff);
  float intensity = clamp((theta - spotlight.outerCutOff) / epsilon, 0.0, 1.0);
  diffuse  *= intensity;
  specular *= intensity;

  return (ambient + diffuse + specular);
}
// Calculates the light impact of the general lights
vec3 calculateLight(Light light, vec3 norm, vec3 viewDir){
    // Ambient
    vec3 ambient = light.ambient * texture(first_texture, aTexCoord).rgb;

    // Diffuse
    vec3 lightDir = normalize(light.position - aPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = light.diffuse * diff * texture(first_texture, aTexCoord).rgb;

    // Specular
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), material.shininess);
    vec3 specular = light.specular * spec * texture(second_texture, aTexCoord).rgb;

    return (ambient + diffuse + specular);
}

void main() {

    vec3 norm = normalize(aNormal);
    vec3 viewDir = normalize(viewPos - aPos);

    // Calculates impact of first general light
    vec3 result = calculateLight(light1, norm, viewDir);

    // Calculates impact of second general light
    result += calculateLight(light2, norm, viewDir);

    result += calculateSpotlight(spotlight1, norm, viewDir);

    // TODO calculate spotlight

    fragColor = vec4(result, 1.0);
}