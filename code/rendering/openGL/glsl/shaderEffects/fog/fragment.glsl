#version 450

in vec2 textCoords;

uniform sampler2D textureSampler;

out vec4 FragColour;

void main() {
    FragColour = texture(textureSampler,textCoords);
}
