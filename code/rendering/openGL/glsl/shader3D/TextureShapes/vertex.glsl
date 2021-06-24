#version 450

layout(location=0) in vec3 position;
layout(location=1) in vec2 inTextCoord;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;

out vec2 textCoord;

void main(){
    gl_Position=projectionMatrix * worldMatrix * vec4(position,1);
    textCoord=inTextCoord;
}
