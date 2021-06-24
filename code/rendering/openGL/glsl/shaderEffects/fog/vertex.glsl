#version 450

layout(location=0) in vec2 position;
layout(location=1) in vec2 inTextCoords;

out vec2 textCoords;

void main() {
    gl_Position=vec4(position,0.0,1.0);
    textCoords=inTextCoords;
}
