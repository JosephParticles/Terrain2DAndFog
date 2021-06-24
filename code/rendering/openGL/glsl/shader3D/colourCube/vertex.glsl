#version 450

layout(location=0) in vec3 position;
layout(location=1) in vec3 colour;

uniform float currentTime;
uniform mat4 projectionMatrix;
uniform mat4 worldMatrix;
uniform mat4 transformationMatrix;

out vec4 vertexColour;
out vec3 outPosition;

void main(){

	vec4 transformedPos=transformationMatrix * vec4(position,1);

    gl_Position=projectionMatrix * worldMatrix * transformedPos;
    vertexColour=vec4(colour,1);
    outPosition=transformedPos.xyz;
}
