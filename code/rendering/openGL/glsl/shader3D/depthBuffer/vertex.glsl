#version 450

layout(location=0) in vec3 position;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;
uniform mat4 transformationMatrix;

void main(){
    gl_Position=projectionMatrix * worldMatrix * transformationMatrix * vec4(position,1);
}