#version 450

layout(location=0) in vec3 position;
layout(location=1) in vec3 colour;

uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;
uniform mat4 transformationMatrix;

out vec3 vertexPosition;
out vec3 vertexColour;

void main(){

	vec4 transformedPos=transformationMatrix * vec4(position,1);

    gl_Position=projectionMatrix * worldMatrix * transformedPos;
    vertexPosition=transformedPos.xyz;
    vertexColour=colour;
}