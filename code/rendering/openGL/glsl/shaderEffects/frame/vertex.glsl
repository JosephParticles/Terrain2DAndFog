#version 450
layout (location = 0) in vec2 position;
layout (location = 1) in vec2 textureCoords;

out vec2 fragTextureCoordinates;

void main() {
    gl_Position=vec4(position,0,1);
    fragTextureCoordinates=textureCoords;
}
